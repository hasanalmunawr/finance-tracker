package hasanalmunawr.dev.backend_spring.budget.service;

import hasanalmunawr.dev.backend_spring.budget.api.BudgetHeaderRequest;
import org.springframework.http.ResponseEntity;

public interface BudgetHeaderService {

    ResponseEntity<?> createBudgetHeader(BudgetHeaderRequest request);
    ResponseEntity<?> getMyBudgetHeaders(String sort, String filter);
    ResponseEntity<?> getBudgetById(Integer id);
    ResponseEntity<?> updateBudgetHeader(Integer id, BudgetHeaderRequest request);
    ResponseEntity<?> deleteBudgetHeader(Integer id);

}
