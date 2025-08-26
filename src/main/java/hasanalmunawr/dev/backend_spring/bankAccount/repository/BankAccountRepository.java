package hasanalmunawr.dev.backend_spring.bankAccount.repository;

import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BankAccountRepository extends BaseRepository<BankAccount> {

    @Query(
            value = """
        SELECT * FROM bank_accounts 
        WHERE LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) 
        """,
            countQuery = """
         SELECT * FROM bank_accounts 
        WHERE LOWER(name) LIKE LOWER(CONCAT('%', :search, '%')) 
        """,
            nativeQuery = true
    )
    Page<BankAccount> searchBankAccount(@Param("search") String search, Pageable pageable);

    @Query(
            value = "SELECT * FROM bank_accounts WHERE user_id = :userId",
            countQuery = "SELECT count(*) FROM bank_accounts WHERE user_id = :userId",
            nativeQuery = true
    )
    Page<BankAccount> findByUserId(@Param("userId") Integer userId, Pageable pageable);

    @Query(
            value = "SELECT * FROM bank_accounts WHERE user_id = :userId",
            countQuery = "SELECT count(*) FROM bank_accounts WHERE user_id = :userId",
            nativeQuery = true
    )
    List<BankAccount> findByUserId(@Param("userId") Integer userId);
}
