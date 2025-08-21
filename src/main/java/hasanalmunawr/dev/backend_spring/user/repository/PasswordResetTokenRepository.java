package hasanalmunawr.dev.backend_spring.user.repository;

import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.user.model.PasswordResetTokenModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenRepository extends BaseRepository<PasswordResetTokenModel> {

    Optional<PasswordResetTokenModel> findByToken(String token);

}
