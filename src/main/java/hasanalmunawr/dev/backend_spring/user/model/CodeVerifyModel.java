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
@Table(name = "code_verifies")
@Entity
public class CodeVerifyModel extends BaseModel {

    @Column(unique = true, nullable = false)
    private String code;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
}
