package hasanalmunawr.dev.backend_spring.transaction.controller;

import hasanalmunawr.dev.backend_spring.base.constants.Endpoint;
import hasanalmunawr.dev.backend_spring.debts.api.DebtRequest;
import hasanalmunawr.dev.backend_spring.transaction.api.TransactionRequest;
import hasanalmunawr.dev.backend_spring.transaction.service.TransactionService;
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

@RestController
@RequestMapping(Endpoint.Base.TRANSACTION)
@Tag(name = "Transaction Controller", description = "API for managing transaction related operations.")
public class TransactionController {

    @Autowired
    public TransactionService transactionService;

    @GetMapping()
    public ResponseEntity<?> getAllTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return transactionService.getAllTransactions(page, size, sort, filter);
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String month
    ) {
        return transactionService.getMyTransactions(page, size, sort, filter, month);
    }

    @GetMapping("/mine-2")
    public ResponseEntity<?> getMyTransactions2(
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String month,
            @RequestParam(required = false) Integer bankId
    ) {
        return transactionService.getMyTransactions2(sort, filter, month, bankId);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Integer id) {
        return transactionService.getTransactionById(id);
    }


    @PostMapping(Endpoint.Basic.CREATE)
    public ResponseEntity<?> createTransaction(
            @Valid @RequestBody TransactionRequest request
    ) {
        return transactionService.createTransaction(request);
    }

}
