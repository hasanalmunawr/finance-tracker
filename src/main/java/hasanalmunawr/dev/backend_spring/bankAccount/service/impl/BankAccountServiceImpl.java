package hasanalmunawr.dev.backend_spring.bankAccount.service.impl;

import hasanalmunawr.dev.backend_spring.bankAccount.api.request.BankAccountRequest;
import hasanalmunawr.dev.backend_spring.bankAccount.api.response.BankAccountResponse;
import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.bankAccount.service.BankAccountService;
import hasanalmunawr.dev.backend_spring.base.api.PaginationResponse;
import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import hasanalmunawr.dev.backend_spring.web.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final GeneralRepository generalRepository;
    private final TaskProcessor taskProcessor;
    private final CurrentUserService currentUserService;

    @Override
    public ResponseEntity<?> createBankAccount(BankAccountRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            BankAccount bank = new BankAccount()
                    .setName(request.getName())
                    .setNumber(request.getNumber())
                    .setBalance(request.getBalance())
                    .setUser(currentUser);

            generalRepository.getBankAccountRepository().save(bank);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, BankAccountResponse.fromModel(bank));
        });
    }

    @Override
    public ResponseEntity<?> getAllBankAccounts(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            Page<BankAccount> bankAccountsData = getBankAccounts(page, size, sort, filter);

            List<BankAccountResponse> serviceOrderContent = bankAccountsData.getContent()
                    .stream()
                    .map(BankAccountResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(bankAccountsData.getTotalPages())
                    .setTotalData(bankAccountsData.getTotalElements())
                    .setCurrentPage(bankAccountsData.getNumber() + 1)
                    .setData(serviceOrderContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyBankAccounts(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            Page<BankAccount> bankAccountsData = getMyBankAccounts(currentUser, page, size, sort);

            List<BankAccountResponse> serviceOrderContent = bankAccountsData.getContent()
                    .stream()
                    .map(BankAccountResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(bankAccountsData.getTotalPages())
                    .setTotalData(bankAccountsData.getTotalElements())
                    .setCurrentPage(bankAccountsData.getNumber() + 1)
                    .setData(serviceOrderContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyBankAccounts2(String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            List<BankAccountResponse> responses = generalRepository.getBankAccountRepository()
                    .findByUserId(currentUser.getId())
                    .stream()
                    .map(BankAccountResponse::fromModel).toList();

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, responses);
        });
    }

    @Override
    public ResponseEntity<?> getBankAccountById(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            BankAccount bankAccount = generalRepository.getBankAccountRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, BankAccountResponse.fromModel(bankAccount));
        });
    }

    @Override
    public ResponseEntity<?> updateBankAccount(Integer id, BankAccountRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            BankAccount bankAccount = generalRepository.getBankAccountRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            updateBankAccountFromRequest(bankAccount, request);
            generalRepository.getBankAccountRepository().save(bankAccount);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_UPDATED, null);
        });
    }

    @Override
    public ResponseEntity<?> deleteBankAccount(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            Optional<BankAccount> bankAccount = generalRepository.getBankAccountRepository().findById(id);

            if (!bankAccount.isPresent()) throw new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND);

            generalRepository.getBankAccountRepository().deleteById(id);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_DELETED, null);
        });
    }

    private Page<BankAccount> getBankAccounts(int page, int size, String sort ,String filter) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getBankAccountRepository().searchBankAccount(filter, pageable);
    }

    private Page<BankAccount> getMyBankAccounts(UserModel user, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getBankAccountRepository().findByUserId(user.getId(), pageable);
    }

    private void updateBankAccountFromRequest(BankAccount bankAccount, BankAccountRequest bankAccountRequest) {
        bankAccount.setName(bankAccountRequest.getName());
        bankAccount.setNumber(bankAccountRequest.getNumber());
        bankAccount.setBalance(bankAccountRequest.getBalance());
    }


}
