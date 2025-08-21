package hasanalmunawr.dev.backend_spring.user.repository;

import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends BaseRepository<UserModel> {

    Optional<UserModel> findByEmail(String email);

    Optional<UserModel> findByUsername(String username);

}
