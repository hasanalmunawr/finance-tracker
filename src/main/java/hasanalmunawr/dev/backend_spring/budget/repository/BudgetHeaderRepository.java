package hasanalmunawr.dev.backend_spring.budget.repository;

import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetHeaderModel;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetHeaderRepository extends BaseRepository<BudgetHeaderModel> {

    Optional<BudgetHeaderModel> findBySlug(String slug);

    @Query(
            value = "SELECT * FROM budget_headers WHERE user_id = :userId AND is_deleted = false",
            countQuery = "SELECT count(*) FROM budget_headers WHERE user_id = :userId  AND is_deleted = false",
            nativeQuery = true
    )
    List<BudgetHeaderModel> findByUserId(@Param("userId") Integer userId);
}
