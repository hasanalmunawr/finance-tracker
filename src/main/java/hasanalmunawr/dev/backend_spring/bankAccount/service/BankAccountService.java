package hasanalmunawr.dev.backend_spring.bankAccount.service;

import hasanalmunawr.dev.backend_spring.bankAccount.api.request.BankAccountRequest;
import org.springframework.http.ResponseEntity;

public interface BankAccountService {

    ResponseEntity<?> createBankAccount(BankAccountRequest request);
    ResponseEntity<?> getAllBankAccounts(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyBankAccounts(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyBankAccounts2(String sort, String filter);
    ResponseEntity<?> getBankAccountById(Integer id);
    ResponseEntity<?> updateBankAccount(Integer id, BankAccountRequest request);
    ResponseEntity<?> deleteBankAccount(Integer id);

}
