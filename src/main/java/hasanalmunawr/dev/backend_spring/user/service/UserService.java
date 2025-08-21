package hasanalmunawr.dev.backend_spring.user.service;

import hasanalmunawr.dev.backend_spring.user.api.request.*;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> registerUser(RegisterRequest request);

    ResponseEntity<?> login(LoginRequest loginRequest);

    ResponseEntity<?> refreshToken(RefreshTokenRequest refreshTokenRequest);

    ResponseEntity<?> verifyEmailCode(VerifyCodeRequest verifyCodeRequest);

    ResponseEntity<?> forgotPassword(ForgotPasswordRequest resetPasswordRequest);

    ResponseEntity<?> resetPassword(ResetPasswordRequest resetPasswordRequest);

    ResponseEntity<?> resendEmailCode(String email);

    ResponseEntity<?> profile();

    ResponseEntity<?> updateProfile(UpdateProfileRequest updateProfileRequest);

    ResponseEntity<?> updatePassword(UpdatePasswordRequest updatePasswordRequest);
}
