package hasanalmunawr.dev.backend_spring.dashboard.service;

import org.springframework.http.ResponseEntity;

public interface DashboardService {

    ResponseEntity<?> getTransactionSummaryDashboard();
    ResponseEntity<?> getExpenseCategoryDashboard(Integer year, Integer month);
    ResponseEntity<?> getAvgSpending(Integer year, Integer month);
    ResponseEntity<?> getTotalIncome(Integer year, Integer month);
    ResponseEntity<?> getTotalExpense(Integer year, Integer month);
    ResponseEntity<?> getCashFlow(Integer year, Integer month);
}
