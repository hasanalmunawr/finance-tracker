package hasanalmunawr.dev.backend_spring.dashboard.repository;

import hasanalmunawr.dev.backend_spring.base.repository.BaseRepository;
import hasanalmunawr.dev.backend_spring.dashboard.api.ExpenseCategorySummary;
import hasanalmunawr.dev.backend_spring.dashboard.api.TransactionSummaryResponse;
import hasanalmunawr.dev.backend_spring.transaction.model.TransactionModel;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DashboardRepository extends BaseRepository<TransactionModel> {

    @Query(value = """
                SELECT 
                    EXTRACT(MONTH FROM t.date) AS month,
                    EXTRACT(YEAR FROM t.date) AS year,
                    t.type AS type,
                    SUM(t.amount) AS totalAmount,
                    COUNT(*) AS totalTransactions
                FROM transactions t
                WHERE t.user_id = :userId
                GROUP BY EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date), t.type
                ORDER BY EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date)
            """, nativeQuery = true)
    List<TransactionSummaryResponse> getTransactionSummaryByUser(@Param("userId") Integer userId);

    @Query(value = """
                SELECT 
                    c.name AS category,
                    SUM(t.amount) AS category_total,
                    COUNT(*) AS total_transactions
                FROM transactions t
                JOIN categories c ON t.category_id = c.id
                WHERE t.user_id = :userId
                  AND t.type = 'Expense'
                  AND EXTRACT(YEAR FROM t.date) = :year
                  AND EXTRACT(MONTH FROM t.date) = :month
                GROUP BY c.name
                ORDER BY category_total DESC
            """, nativeQuery = true)
    List<ExpenseCategorySummary> getExpenseSummaryByCategory(
            @Param("userId") Integer userId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

    @Query(value = """
                SELECT SUM(t.amount) AS total
                FROM transactions t
                WHERE t.user_id = :userId
                  AND t.type = 'Expense'
                  AND EXTRACT(YEAR FROM t.date) = :year
                  AND EXTRACT(MONTH FROM t.date) = :month
                GROUP BY EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date)
                ORDER BY EXTRACT(YEAR FROM t.date), EXTRACT(MONTH FROM t.date)
            """, nativeQuery = true)
    List<BigDecimal> findMonthlyExpenseTotalsByUserAndYear(
            @Param("userId") Integer userId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

    @Query(value = """
                SELECT COALESCE(SUM(t.amount), 0) AS total
                                                                              FROM transactions t
                                                                              WHERE t.user_id = :userId
                                                                                AND t.type = 'Income'
                                                                                AND EXTRACT(YEAR FROM t.date) = :year
                                                                                AND EXTRACT(MONTH FROM t.date) = :month
                                                                                                                   
            """, nativeQuery = true)
    List<BigDecimal> getMonthlyIncome(
            @Param("userId") Integer userId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

    @Query(value = """
                SELECT COALESCE(SUM(t.amount), 0) AS total
                                                                              FROM transactions t
                                                                              WHERE t.user_id = :userId
                                                                                AND t.type = 'Expense'
                                                                                AND EXTRACT(YEAR FROM t.date) = :year
                                                                                AND EXTRACT(MONTH FROM t.date) = :month
                                                                                                                   
            """, nativeQuery = true)
    List<BigDecimal> getMonthlyExpense(
            @Param("userId") Integer userId,
            @Param("year") Integer year,
            @Param("month") Integer month
    );

}
