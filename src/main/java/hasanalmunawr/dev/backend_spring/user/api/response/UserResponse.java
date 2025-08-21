package hasanalmunawr.dev.backend_spring.user.api.response;

import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import hasanalmunawr.dev.backend_spring.web.model.Role;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponse {

    private Integer id;
    private String username;
    private String email;
    private Role role;
    private Boolean isEnabled;

    public static UserResponse fromModel(UserModel user) {
        return new UserResponse()
                .setId(user.getId())
                .setUsername(user.getFullName())
                .setEmail(user.getEmail())
                .setRole(user.getRole())
                .setIsEnabled(user.isEnabled());
    }
}
