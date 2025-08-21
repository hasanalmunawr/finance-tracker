package hasanalmunawr.dev.backend_spring.budget.controller;

import hasanalmunawr.dev.backend_spring.base.constants.Endpoint;
import hasanalmunawr.dev.backend_spring.budget.api.BudgetRequest;
import hasanalmunawr.dev.backend_spring.budget.service.BudgetService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.Base.BUDGET)
@Tag(name = "Budget Controller", description = "API for managing service-order-related operations.")
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @GetMapping()
    public ResponseEntity<?> getAllBudgets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return budgetService.getAllBudgets(page, size, sort, filter);
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyBudgets(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return budgetService.getMyBudgets(page, size, sort, filter);
    }

    @GetMapping("/mine-2")
    public ResponseEntity<?> getMyBudgets2(
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return budgetService.getMyBudgets2(sort, filter);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBudgetById(@PathVariable Integer id) {
        return budgetService.getBudgetById(id);
    }

     @GetMapping("/header/{id}")
    public ResponseEntity<?> getBudgetByHeaderId(@PathVariable Integer id) {
        return budgetService.getBudgetByHeaderId(id);
    }


    @PostMapping(Endpoint.Basic.CREATE)
    public ResponseEntity<?> createBudget(
            @Valid @RequestBody BudgetRequest request
    ) {
        return budgetService.createBudget(request);
    }


    @PutMapping(Endpoint.Basic.UPDATE + "/{id}")
    public ResponseEntity<?> updateBudget(@PathVariable Integer id, @Valid @RequestBody BudgetRequest request) {
        return budgetService.updateBudget(id, request);
    }

    @DeleteMapping(Endpoint.Basic.DELETE + "/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable Integer id) {
        return budgetService.deleteBudget(id);
    }

}
