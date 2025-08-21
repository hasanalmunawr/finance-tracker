package hasanalmunawr.dev.backend_spring.user.service.impl;

import hasanalmunawr.dev.backend_spring.user.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailServiceImpl.class);
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

//    @Value("${spring.mail.username}")
//    private String senderEmail;
//
    @Value("${application.mailing.frontend.activation-url}")
    private String frontendUrl;

    @Override
    public void sendCodeVerifyEmail(
            String to,
            String username,
            String activationCode,
            String subject
    ) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()
            );

            // Set email template variables
            Map<String, Object> properties = new HashMap<>();
            properties.put("username", username);
            properties.put("activation_code", activationCode);
            properties.put("current_year", getCurrentYear());

            Context context = new Context();
            context.setVariables(properties);

            // Process HTML template
            String htmlContent = templateEngine.process("verify-email", context);

            // Set email attributes
            helper.setFrom("hasanalmunawar.it@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to construct or send email", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while sending email", e);
        }
    }

    @Override
    public void sendResetPasswordEmail(
            String to,
            String username,
            String tokenResetPassword,
            String subject
    ) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
                    mimeMessage,
                    MimeMessageHelper.MULTIPART_MODE_MIXED,
                    StandardCharsets.UTF_8.name()
            );


            String linkResetPasswordUrl = frontendUrl + "/reset-password?token=" + tokenResetPassword;
            log.info("link reset password url: {}", linkResetPasswordUrl);
            // Set email template variables
            Map<String, Object> properties = new HashMap<>();
            properties.put("username", username);
            properties.put("reset_link", linkResetPasswordUrl);
            properties.put("current_year", getCurrentYear());

            Context context = new Context();
            context.setVariables(properties);

            // Process HTML template
            String htmlContent = templateEngine.process("reset-password", context);

            // Set email attributes
            helper.setFrom("hasanalmunawar.it@gmail.com");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            // Send email
            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new RuntimeException("Failed to construct or send email", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error occurred while sending email", e);
        }
    }


    public int getCurrentYear() {
        return Year.now().getValue();
    }
//    @Override
//    public void sendEmailGoogle(String to, String username, EmailTemplateName emailTemplate, String activationCode, String subject) throws MessagingException {
//        String templateName;
//        if (emailTemplate == null) {
//            templateName = "confirm-email";
//        } else {
//            templateName = emailTemplate.getName();
//        }
//
//        MimeMessage mimeMessage = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(
//                mimeMessage,
//                MULTIPART_MODE_MIXED,
//                UTF_8.name()
//        );
//        Map<String, Object> properties = new HashMap<>();
//        properties.put("username", username);
//        properties.put("confirmationUrl", confirmationUrl);
//        properties.put("activation_code", activationCode);
//
//        Context context = new Context();
//        context.setVariables(properties);
//
//        helper.setFrom(senderEmail);
//        helper.setTo(to);
//        helper.setSubject(subject);
//
//        String template = templateEngine.process(templateName, context);
//        helper.setText(template, true);
//        mailSender.send(mimeMessage);
//    }
}
