package hasanalmunawr.dev.backend_spring.transaction.service.impl;

import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.bankAccount.repository.BankAccountRepository;
import hasanalmunawr.dev.backend_spring.base.api.PaginationResponse;
import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.debts.api.DebtResponse;
import hasanalmunawr.dev.backend_spring.debts.model.DebtModel;
import hasanalmunawr.dev.backend_spring.transaction.api.TransactionRequest;
import hasanalmunawr.dev.backend_spring.transaction.api.TransactionResponse;
import hasanalmunawr.dev.backend_spring.transaction.model.TransactionModel;
import hasanalmunawr.dev.backend_spring.transaction.service.TransactionService;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import hasanalmunawr.dev.backend_spring.web.service.CurrentUserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {

    private final GeneralRepository generalRepository;
    private final TaskProcessor taskProcessor;
    private final CurrentUserService currentUserService;
    private final TransactionHelperService transactionHelperService;

    @Override
    public ResponseEntity<?> createTransaction(TransactionRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            CategoryModel category = generalRepository.getCategoryRepository().findById(request.getCategory_id())
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            transactionHelperService.processTransaction(request.getFrom_bank_account_id(), request.getTo_bank_account_id(), request.getType(), request.getAmount());

            BankAccount toBankAccount = getBankHelper(request.getTo_bank_account_id()); // ini juga ada
            BankAccount fromBankAccount = getBankHelper(request.getFrom_bank_account_id()); // ini ada
            log.info("[TransactionService::createTransaction] Saving transaction {}", toBankAccount);
            TransactionModel transaction = new TransactionModel()
                    .setType(request.getType())
                    .setDate(request.getDate())
                    .setAmount(request.getAmount())
                    .setNotes(request.getNotes())
                    .setToBankAccount(toBankAccount) // di database null
                    .setFromBankAccount(fromBankAccount) // ini juga di database null
                    .setCategory(category)
                    .setUser(currentUser);

            generalRepository.getTransactionRepository().save(transaction);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, TransactionResponse.fromModel(transaction));
        });
    }

    @Override
    public ResponseEntity<?> getAllTransactions(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            Page<TransactionModel> transactionData = getTransactions(page, size, sort, filter);

            List<TransactionResponse> transactionContent = transactionData.getContent()
                    .stream()
                    .map(TransactionResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(transactionData.getTotalPages())
                    .setTotalData(transactionData.getTotalElements())
                    .setCurrentPage(transactionData.getNumber() + 1)
                    .setData(transactionContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyTransactions(int page, int size, String sort, String filter, String yearMonth) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            Page<TransactionModel> transactionData = getMyTransactionsHelper(currentUser, page, size, sort, yearMonth);

            List<TransactionResponse> transactionResponse = transactionData.getContent()
                    .stream()
                    .map(TransactionResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(transactionData.getTotalPages())
                    .setTotalData(transactionData.getTotalElements())
                    .setCurrentPage(transactionData.getNumber() + 1)
                    .setData(transactionResponse);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyTransactions2(String sort, String filter, String yearMonth, Integer bankFilter) {
        return taskProcessor.executeResponseHttp(() -> {
            String[] parts = yearMonth.split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            UserModel currentUser = currentUserService.getCurrentUser();

            List<TransactionModel> transactionModels = generalRepository.getTransactionRepository()
                    .findByUserId(currentUser.getId(), year, month, bankFilter);

            List<TransactionResponse> responses = transactionModels.stream()
                    .map(TransactionResponse::fromModel)
                    .toList();

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, responses);
        });
    }

    @Override
    public ResponseEntity<?> getTransactionById(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            TransactionModel transaction = generalRepository.getTransactionRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, TransactionResponse.fromModel(transaction));
        });
    }


    private BankAccount getBankHelper(Integer bankId) {
        return generalRepository.getBankAccountRepository().findById(bankId)
                .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));
    }

    private Page<TransactionModel> getTransactions(int page, int size, String sort , String filter) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getTransactionRepository().searchTransactions(filter, pageable);
    }

    private Page<TransactionModel> getMyTransactionsHelper(UserModel user, int page, int size, String sort, String yearMonth) {
        String[] parts = yearMonth.split("-");
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getTransactionRepository().findByUserId(user.getId(), year, month, pageable);
    }
}
