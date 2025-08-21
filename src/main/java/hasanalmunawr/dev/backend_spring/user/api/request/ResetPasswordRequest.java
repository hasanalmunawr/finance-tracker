package hasanalmunawr.dev.backend_spring.user.api.request;

import lombok.Data;

@Data
public class ResetPasswordRequest {

    private String token;
    private String newPassword;

}
