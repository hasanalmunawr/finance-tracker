package hasanalmunawr.dev.backend_spring.transaction.service.impl;

import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.bankAccount.repository.BankAccountRepository;
import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.service.annotation.GetExchange;

import java.math.BigDecimal;

@Service
@Slf4j
public class TransactionHelperService {

    @Autowired
    private GeneralRepository generalRepository;

    @Transactional
    public void processTransaction(Integer fromBankId, Integer toBankId, String type, BigDecimal amount) {
        BankAccountRepository bankRepo = generalRepository.getBankAccountRepository();

        switch (type) {
            case "Income":
                BankAccount incomeAccount = bankRepo.findById(fromBankId)
                        .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

                incomeAccount.setBalance(incomeAccount.getBalance().add(amount));
                bankRepo.save(incomeAccount);
                break;

            case "Expense":
                BankAccount expenseAccount = bankRepo.findById(fromBankId)
                        .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

                BigDecimal newExpenseBalance = expenseAccount.getBalance().subtract(amount);

                if (newExpenseBalance.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException("Insufficient balance for expense.");
                }

                expenseAccount.setBalance(newExpenseBalance);
                bankRepo.save(expenseAccount);
                break;

            case "Mutation":
                BankAccount sender = bankRepo.findById(fromBankId)
                        .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));
                log.info("Bank Sender is Found {}", sender);

                BankAccount receiver = bankRepo.findById(toBankId)
                        .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));
                log.info("Bank Receiver is Found {}", receiver);

                if (sender.getBalance().compareTo(amount) < 0) {
                    throw new IllegalArgumentException("Insufficient balance for mutation.");
                }
                log.info("Bank Sender Balance is (passed condition) {}", sender.getBalance());
                sender.setBalance(sender.getBalance().subtract(amount));
                log.info("Bank Sender Balance is (passed subtract code) {}", sender.getBalance());
                receiver.setBalance(receiver.getBalance().add(amount));
                log.info("Bank Receiver Balance is (passed add code) {}", receiver.getBalance());
                bankRepo.save(sender);
                bankRepo.save(receiver);
                log.info("Both Bank Accounts have been saved");
                break;

            default:
                throw new IllegalArgumentException("Invalid transaction type: " + type);
        }
    }
}
