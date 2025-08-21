package hasanalmunawr.dev.backend_spring.user.api.request;

import lombok.Data;

@Data
public class UpdateProfileRequest {

    private String username;
    private String email;
    private String phone;

}
