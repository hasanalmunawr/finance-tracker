package hasanalmunawr.dev.backend_spring.budget.service;

import hasanalmunawr.dev.backend_spring.budget.api.BudgetRequest;
import org.springframework.http.ResponseEntity;

public interface BudgetService {

    ResponseEntity<?> createBudget(BudgetRequest request);
    ResponseEntity<?> getAllBudgets(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyBudgets(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyBudgets2(String sort, String filter);
    ResponseEntity<?> getBudgetById(Integer id);
    ResponseEntity<?> getBudgetByHeaderId(Integer id);
    ResponseEntity<?> updateBudget(Integer id, BudgetRequest request);
    ResponseEntity<?> deleteBudget(Integer id);

}
