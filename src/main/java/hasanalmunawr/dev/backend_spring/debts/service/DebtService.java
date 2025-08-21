package hasanalmunawr.dev.backend_spring.debts.service;

import hasanalmunawr.dev.backend_spring.budget.api.BudgetRequest;
import hasanalmunawr.dev.backend_spring.debts.api.DebtRequest;
import org.springframework.http.ResponseEntity;

public interface DebtService {

    ResponseEntity<?> createDebt(DebtRequest request);
    ResponseEntity<?> getAllDebts(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyDebts(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyDebts2(String sort, String filter);
    ResponseEntity<?> getDebtById(Integer id);
    ResponseEntity<?> updateDebt(Integer id, DebtRequest request);
    ResponseEntity<?> deleteDebt(Integer id);

}
