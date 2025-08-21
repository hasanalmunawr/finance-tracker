package hasanalmunawr.dev.backend_spring.web.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static hasanalmunawr.dev.backend_spring.web.model.Permission.*;

@RequiredArgsConstructor
public enum Role {

    USER(Set.of(
            USER_READ,
            USER_UPDATE,
            DATA_INPUT,
            DATA_UPDATE
    )),

    ADMIN_SUPER(Set.of(
            USER_READ,
            USER_UPDATE,
            DATA_INPUT,
            DATA_UPDATE,
            DASHBOARD_VIEW,
            REPORT_VIEW,
            ADMIN_READ,
            ADMIN_CREATE,
            ADMIN_UPDATE,
            ADMIN_DELETE
    ));

    @Getter
    private final Set<Permission> permissions;

    public String getDescription() {
        return switch (this) {
            case USER -> "User Common";
            case ADMIN_SUPER -> "Administrator Full";
        };
    }


    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions().stream()
                .map(p -> new SimpleGrantedAuthority(p.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }

}
