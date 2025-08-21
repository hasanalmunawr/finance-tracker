package hasanalmunawr.dev.backend_spring.transaction.repository;

import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.debts.model.DebtModel;
import hasanalmunawr.dev.backend_spring.transaction.model.TransactionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends BaseRepository<TransactionModel> {

    @Query(
            value = """
        SELECT * FROM transactions 
        WHERE LOWER(type) LIKE LOWER(CONCAT('%', :search, '%')) 
        """,
            countQuery = """
         SELECT * FROM transactions 
        WHERE LOWER(type) LIKE LOWER(CONCAT('%', :search, '%')) 
        """,
            nativeQuery = true
    )
    Page<TransactionModel> searchTransactions(@Param("search") String search, Pageable pageable);

    @Query(
            value = "SELECT * FROM transactions " +
                    "WHERE user_id = :userId " +
                    "AND YEAR(date) = :year " +
                    "AND MONTH(date) = :month",
            countQuery = "SELECT count(*) FROM transactions " +
                    "WHERE user_id = :userId " +
                    "AND YEAR(date) = :year " +
                    "AND MONTH(date) = :month",
            nativeQuery = true
    )
    Page<TransactionModel> findByUserId(
            @Param("userId") Integer userId,
            @Param("year") int year,
            @Param("month") int month,
            Pageable pageable
    );

    @Query(
            value = "SELECT * FROM transactions " +
                    "WHERE user_id = :userId " +
                    "AND YEAR(date) = :year " +
                    "AND MONTH(date) = :month " +
                    "AND (:bankAccountId = 0 OR from_bank_account_id = :bankAccountId) " +
                    "ORDER BY date DESC",
            countQuery = "SELECT count(*) FROM transactions " +
                    "WHERE user_id = :userId " +
                    "AND YEAR(date) = :year " +
                    "AND MONTH(date) = :month " +
                    "AND (:bankAccountId = 0 OR from_bank_account_id = :bankAccountId)",
            nativeQuery = true
    )
    List<TransactionModel> findByUserId(
            @Param("userId") Integer userId,
            @Param("year") int year,
            @Param("month") int month,
            @Param("bankAccountId") Integer bankAccountId // jika ini 0 maka ambil semua,
    );
}
