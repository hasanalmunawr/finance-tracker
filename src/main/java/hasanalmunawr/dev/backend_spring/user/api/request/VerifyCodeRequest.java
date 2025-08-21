package hasanalmunawr.dev.backend_spring.user.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerifyCodeRequest {

    @NotBlank(message = "Code is required")
    private String code;

}
