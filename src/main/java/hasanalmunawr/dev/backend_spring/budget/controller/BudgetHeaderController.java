package hasanalmunawr.dev.backend_spring.budget.controller;

import hasanalmunawr.dev.backend_spring.base.constants.Endpoint;
import hasanalmunawr.dev.backend_spring.budget.api.BudgetHeaderRequest;
import hasanalmunawr.dev.backend_spring.budget.api.BudgetRequest;
import hasanalmunawr.dev.backend_spring.budget.service.BudgetHeaderService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Endpoint.Base.BUDGET_HEADER)
@Tag(name = "Budget Header Controller", description = "API for managing service-order-related operations.")
public class BudgetHeaderController {

    @Autowired
    private BudgetHeaderService budgetHeaderService;

    @GetMapping("/mine")
    public ResponseEntity<?> getMyBudgetHeaders(
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return budgetHeaderService.getMyBudgetHeaders(sort, filter);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBudgetHeaderById(@PathVariable Integer id) {
        return budgetHeaderService.getBudgetById(id);
    }

    @PostMapping(Endpoint.Basic.CREATE)
    public ResponseEntity<?> createBudget(
            @Valid @RequestBody BudgetHeaderRequest request
    ) {
        return budgetHeaderService.createBudgetHeader(request);
    }

    @PutMapping(Endpoint.Basic.UPDATE + "/{id}")
    public ResponseEntity<?> updateBudget(
            @PathVariable Integer id,
            @Valid @RequestBody BudgetHeaderRequest request
    ) {
        return budgetHeaderService.updateBudgetHeader(id, request);
    }

    @DeleteMapping(Endpoint.Basic.DELETE + "/{id}")
    public ResponseEntity<?> deleteBudget(@PathVariable Integer id) {
        return budgetHeaderService.deleteBudgetHeader(id);
    }

}
