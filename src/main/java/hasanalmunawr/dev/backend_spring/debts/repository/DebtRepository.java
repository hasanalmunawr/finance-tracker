package hasanalmunawr.dev.backend_spring.debts.repository;

import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetModel;
import hasanalmunawr.dev.backend_spring.debts.model.DebtModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DebtRepository extends BaseRepository<DebtModel> {

    @Query(
            value = """
        SELECT * FROM debts 
        WHERE LOWER(contact_name) LIKE LOWER(CONCAT('%', :search, '%')) 
        """,
            countQuery = """
         SELECT * FROM debts 
        WHERE LOWER(contact_name) LIKE LOWER(CONCAT('%', :search, '%')) 
        """,
            nativeQuery = true
    )
    Page<DebtModel> searchDebts(@Param("search") String search, Pageable pageable);

    @Query(
            value = "SELECT * FROM debts WHERE user_id = :userId",
            countQuery = "SELECT count(*) FROM debts WHERE user_id = :userId",
            nativeQuery = true
    )
    Page<DebtModel> findByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query(
            value = "SELECT * FROM debts WHERE user_id = :userId AND is_deleted = false",
            countQuery = "SELECT count(*) FROM debts WHERE user_id = :userId AND is_deleted = false",
            nativeQuery = true
    )
    List<DebtModel> findByUserId(@Param("userId") Integer userId);

}
