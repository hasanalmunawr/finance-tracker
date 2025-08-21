package hasanalmunawr.dev.backend_spring.user.repository;

import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.user.model.CodeVerifyModel;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeVerifyRepository extends BaseRepository<CodeVerifyModel> {

    Optional<CodeVerifyModel> findByCode(String code);
}
