package hasanalmunawr.dev.backend_spring.user.service.impl;

import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.CustomApiException;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.helper.RoleHelper;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessorHandler;
import hasanalmunawr.dev.backend_spring.debts.model.DebtModel;
import hasanalmunawr.dev.backend_spring.user.api.request.*;
import hasanalmunawr.dev.backend_spring.user.api.response.UserResponse;
import hasanalmunawr.dev.backend_spring.user.model.CodeVerifyModel;
import hasanalmunawr.dev.backend_spring.user.model.PasswordResetTokenModel;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import hasanalmunawr.dev.backend_spring.user.service.EmailService;
import hasanalmunawr.dev.backend_spring.user.service.UserService;
import hasanalmunawr.dev.backend_spring.web.config.JwtConfig;
import hasanalmunawr.dev.backend_spring.web.service.CurrentUserService;
import jakarta.mail.MessagingException;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.internal.util.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;


import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final TaskProcessor taskProcessor;

    private final GeneralRepository generalRepository;

    private final JwtConfig jwtConfig;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final EmailService emailService;

    private final CurrentUserService currentUserService;


    @Autowired
    private RoleHelper roleHelper;


    @Override
    public ResponseEntity<?> registerUser(RegisterRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            Optional<UserModel> user = generalRepository.getUserRepository().findByEmail(request.getEmail());

            if (user.isPresent()) {
                throw new CustomApiException(ResponseMessage.User.USER_ALREADY_REGISTERED, HttpStatus.BAD_REQUEST);
            }

            UserModel userModel = new UserModel()
                    .setUsername(request.getUsername())
                    .setEmail(request.getEmail())
                    .setPassword(passwordEncoder.encode(request.getPassword())) // di sini
                    .setRole(roleHelper.convertToRole(request.getRole()))
                    .setPhone(request.getPhone())
                    .setEnabled(false);

            UserModel userSaved = generalRepository.getUserRepository().save(userModel);

            sendValidationEmail(userSaved);

            return taskProcessor.success(ResponseMessage.User.USER_REGISTERED, UserResponse.fromModel(userModel));
        });
    }

    @Override
    public ResponseEntity<?> login(LoginRequest loginRequest) {
        return taskProcessor.executeResponseHttp(() -> {
            log.info("User Try To Login with Email {}", loginRequest.getEmail());
            // 1. Authentikasi user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            // 2. Ambil detail user setelah sukses login
            UserModel user = (UserModel) authentication.getPrincipal();

            // 3. Ambil roles user
            List<String> roles = user.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());

            // 4. Tambahkan informasi ke claim JWT jika perlu
            Map<String, Object> claims = new HashMap<>();
            claims.put("roles", roles);
            claims.put("username", user.getUsername());

            // 5. Generate token
            String token = jwtConfig.generateToken(claims, user);
            String refreshToken = jwtConfig.generateRefreshToken(user);

            // 6. Bungkus response
            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("refreshToken", refreshToken);
            result.put("tokenType", "Bearer");
            result.put("expiresIn", jwtConfig.getJwtExpiration());
            result.put("user", Map.of(
                    "username", user.getFullName(),
                    "roles", roles
            ));

            return taskProcessor.success(ResponseMessage.User.USER_LOGIN_SUCCESS, result);
        });
    }

    @Override
    public ResponseEntity<?> refreshToken(RefreshTokenRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            if (jwtConfig.isTokenExpired(request.getRefreshToken())) {
                throw new RuntimeException(ResponseMessage.Authentication.INVALID_TOKEN);
            }

            String email = jwtConfig.extractUsername(request.getRefreshToken());

            UserModel user = generalRepository.getUserRepository().findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.User.USER_NOT_FOUND));

            if (!jwtConfig.isTokenValid(request.getRefreshToken(), user)) {
                throw new RuntimeException(ResponseMessage.Authentication.INVALID_REFRESH_TOKEN);
            }

            String newAccessToken = jwtConfig.generateToken(user);
            String newRefreshToken = jwtConfig.generateRefreshToken(user); // optional

            Map<String, Object> result = new HashMap<>();
            result.put("token", newAccessToken);
            result.put("refreshToken", newRefreshToken);

            return taskProcessor.success(ResponseMessage.User.USER_REFRESH_TOKEN, result);
        });
    }

    @Override
    public ResponseEntity<?> verifyEmailCode(VerifyCodeRequest codeVerify) {
        return taskProcessor.executeResponseHttp(() -> {

            CodeVerifyModel codeVerifyModel = generalRepository.getCodeVerifyRepository().findByCode(codeVerify.getCode())
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.CodeVerify.CODE_NOT_FOUND));

            if (LocalDateTime.now().isAfter(codeVerifyModel.getExpiresAt())) {
                throw new RuntimeException(ResponseMessage.CodeVerify.CODE_EXPIRED);
            }

            UserModel user = codeVerifyModel.getUser();
            user.setEnabled(true);
            codeVerifyModel.setValidatedAt(LocalDateTime.now());
            generalRepository.getCodeVerifyRepository().save(codeVerifyModel);
            generalRepository.getUserRepository().save(user);

            return taskProcessor.success(ResponseMessage.CodeVerify.CODE_VALID, null);
        });
    }

    @Override
    public ResponseEntity<?> forgotPassword(ForgotPasswordRequest resetPasswordRequest) {
        return taskProcessor.executeResponseHttp(() -> {

            Optional<UserModel> userResetPassword = generalRepository.getUserRepository().findByEmail(resetPasswordRequest.getEmail());

            if (!userResetPassword.isPresent()) {
                throw new NotFoundException(ResponseMessage.User.USER_NOT_FOUND);
            }
            sendResetPasswordLink(userResetPassword.get());
            return taskProcessor.success(ResponseMessage.Authentication.RESET_PASSWORD_EMAIL_SENT, null);
        });
    }

    @Override
    public ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest) {
        return taskProcessor.executeResponseHttp(() -> {

            PasswordResetTokenModel resetTokenModel = generalRepository.getPasswordResetTokenRepository().findByToken(resetPasswordRequest.getToken())
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Authentication.RESET_PASSWORD_TOKEN_INVALID));

            if (LocalDateTime.now().isAfter(resetTokenModel.getExpiresAt())) {
                throw new RuntimeException(ResponseMessage.Authentication.RESET_PASSWORD_TOKEN_INVALID);
            }


            if (resetPasswordRequest.getNewPassword().length() < 8) {
                throw new ValidationException("Password too short");
            }

            UserModel userModel = resetTokenModel.getUser();
            userModel.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));

            generalRepository.getUserRepository().save(userModel);
            log.info("Password reset successful for user: {}", userModel.getEmail());
            generalRepository.getPasswordResetTokenRepository().delete(resetTokenModel);
            return taskProcessor.success(ResponseMessage.Authentication.RESET_PASSWORD_SUCCESS, null);
        });
    }

    @Override
    public ResponseEntity<?> resendEmailCode(String email) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel userModel = generalRepository.getUserRepository().findByEmail(email)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.User.USER_NOT_FOUND));

            sendValidationEmail(userModel);

            return taskProcessor.success(ResponseMessage.CodeVerify.CODE_SENT, null);
        });
    }

    @Override
    public ResponseEntity<?> profile() {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, UserResponse.fromModel(currentUser));
        });
    }

    @Override
    public ResponseEntity<?> updateProfile(UpdateProfileRequest updateProfileRequest) {
        return taskProcessor.executeResponseHttp(() -> {
            log.info("[UserService:updateProfile] :: request => {}", updateProfileRequest);
            UserModel currentUser = currentUserService.getCurrentUser();
            currentUser.setUsername(updateProfileRequest.getUsername());
            currentUser.setEmail(updateProfileRequest.getEmail());
            currentUser.setPhone(updateProfileRequest.getPhone());
            generalRepository.getUserRepository().save(currentUser);

            return taskProcessor.success(ResponseMessage.User.USER_UPDATED, null);
        });
    }

    @Override
    public ResponseEntity<?> updatePassword(UpdatePasswordRequest updatePasswordRequest) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            // Validasi password lama
            if (!passwordEncoder.matches(updatePasswordRequest.getOldPassword(), currentUser.getPassword())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The old password is wrong");
            }

            // Validasi password baru (opsional, tapi sangat disarankan)
            if (updatePasswordRequest.getNewPassword().length() < 8) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The new password must be >= 8 characters");
            }

            // Update password
            currentUser.setPassword(passwordEncoder.encode(updatePasswordRequest.getNewPassword()));
            generalRepository.getUserRepository().save(currentUser);

            return taskProcessor.success(ResponseMessage.User.USER_UPDATED, null);
        });
    }

    private void sendResetPasswordLink(UserModel user) {

        String token = generateAndSaveResetPasswordToken(user);
        emailService.sendResetPasswordEmail(
                user.getEmail(),
                user.getFullName(),
                token,
                "Reset Password Link"
        );
    }

    private void sendValidationEmail(UserModel user) {

        String codeVerify = generateAndSaveActivationToken(user);
        emailService.sendCodeVerifyEmail(
                user.getEmail(),
                user.getFullName(),
                codeVerify,
                "Code Verification Email"
        );
    }

    private String generateAndSaveResetPasswordToken(UserModel user) {
        // Generate a token
        String token = generateResetPasswordToken();

        PasswordResetTokenModel passwordResetTokenModel = new PasswordResetTokenModel()
                .setToken(token)
                .setExpiresAt(LocalDateTime.now().plusMinutes(30))
                .setUser(user)
                .setEmail(user.getEmail());

        generalRepository.getPasswordResetTokenRepository().save(passwordResetTokenModel);

        return token;
    }

    private String generateAndSaveActivationToken(UserModel user) {
        // Generate a code
        String generateCode = generateActivationCode();

        CodeVerifyModel codeVerifyModel = new CodeVerifyModel()
                .setCode(generateCode)
                .setUser(user)
                .setExpiresAt(LocalDateTime.now().plusMinutes(60));

        generalRepository.getCodeVerifyRepository().save(codeVerifyModel);

        return generateCode;
    }

    private String generateResetPasswordToken() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private String generateActivationCode() {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        SecureRandom secureRandom = new SecureRandom();

        for (int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
