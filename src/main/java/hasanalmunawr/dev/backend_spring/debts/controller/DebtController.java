package hasanalmunawr.dev.backend_spring.debts.controller;

import hasanalmunawr.dev.backend_spring.base.constants.Endpoint;
import hasanalmunawr.dev.backend_spring.budget.api.BudgetRequest;
import hasanalmunawr.dev.backend_spring.debts.api.DebtRequest;
import hasanalmunawr.dev.backend_spring.debts.service.DebtService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hasanalmunawr.dev.backend_spring.sales.api.swagger.ServiceOrderExample.SERVICE_ORDER_CREATE_RESPONSE_SUCCESS;
import static hasanalmunawr.dev.backend_spring.sales.api.swagger.ServiceOrderExample.SERVICE_ORDER_REQUEST_JSON;

@RestController
@RequestMapping(Endpoint.Base.DEBT)
@Tag(name = "Debt Controller", description = "API for managing service-order-related operations.")
public class DebtController {

    @Autowired
    private DebtService debtService;

    @GetMapping()
    public ResponseEntity<?> getAllDebts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return debtService.getAllDebts(page, size, sort, filter);
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyDebts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return debtService.getMyDebts(page, size, sort, filter);
    }

    @GetMapping("/mine-2")
    public ResponseEntity<?> getMyDebts2(
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return debtService.getMyDebts2(sort, filter);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getDebtById(@PathVariable Integer id) {
        return debtService.getDebtById(id);
    }


    @PostMapping(Endpoint.Basic.CREATE)
    public ResponseEntity<?> createDebt(
            @Valid @RequestBody DebtRequest request
    ) {
        return debtService.createDebt(request);
    }

    @PutMapping(Endpoint.Basic.UPDATE + "/{id}")
    public ResponseEntity<?> updateDebt(@PathVariable Integer id, @Valid @RequestBody DebtRequest request) {
        return debtService.updateDebt(id, request);
    }

    @DeleteMapping(Endpoint.Basic.DELETE + "/{id}")
    public ResponseEntity<?> deleteDebt(@PathVariable Integer id) {
        return debtService.deleteDebt(id);
    }
}
