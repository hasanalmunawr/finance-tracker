package hasanalmunawr.dev.backend_spring.user.model;

import hasanalmunawr.dev.backend_spring.base.model.BaseModel;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(name = "password_reset_tokens")
@Entity
public class PasswordResetTokenModel extends BaseModel {

    private String token;
    private String email;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
}
