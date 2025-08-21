package hasanalmunawr.dev.backend_spring.transaction.service;

import hasanalmunawr.dev.backend_spring.transaction.api.TransactionRequest;
import org.springframework.http.ResponseEntity;

public interface TransactionService {

    ResponseEntity<?> createTransaction(TransactionRequest request);
    ResponseEntity<?> getAllTransactions(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyTransactions(int page, int size, String sort, String filter, String yearMonth);
    ResponseEntity<?> getMyTransactions2(String sort, String filter, String yearMonth, Integer bankFilter);
    ResponseEntity<?> getTransactionById(Integer id);

}
