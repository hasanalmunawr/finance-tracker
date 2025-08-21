package hasanalmunawr.dev.backend_spring.user.service;

import jakarta.mail.MessagingException;

public interface EmailService {

    void sendCodeVerifyEmail(
            String to,
            String username,
            String activationCode,
            String subject
    );
    void sendResetPasswordEmail(
            String to,
            String username,
            String tokenResetPassword,
            String subject
    );

//    void sendEmailGoogle(
//            String to,
//            String username,
//            EmailTemplateName emailTemplate,
//            String activationCode,
//            String subject
//    ) throws MessagingException;

}
