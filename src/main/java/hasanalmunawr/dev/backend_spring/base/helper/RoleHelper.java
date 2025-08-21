package hasanalmunawr.dev.backend_spring.base.helper;

import hasanalmunawr.dev.backend_spring.web.model.Role;
import org.springframework.stereotype.Component;

@Component
public class RoleHelper {


    /**
     * Converts a string value to its corresponding {@link Role} enum.
     * <p>
     * Supported values (case-insensitive): "admin", "manager", "staff", "user".
     * If the provided string does not match any valid role, an {@link IllegalArgumentException} is thrown.
     *
     * @param role the string representation of the role
     * @return the corresponding {@link Role} enum
     * @throws IllegalArgumentException if the input string does not match any known role
     */
    public Role convertToRole(String role) {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role cannot be null or empty");
        }

        return switch (role.toLowerCase()) {
            case "admin" -> Role.ADMIN_SUPER;
            case "user" -> Role.USER;
            default -> throw new IllegalArgumentException("Invalid role: " + role);
        };
    }

}
