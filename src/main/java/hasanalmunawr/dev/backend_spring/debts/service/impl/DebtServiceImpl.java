package hasanalmunawr.dev.backend_spring.debts.service.impl;

import hasanalmunawr.dev.backend_spring.base.api.PaginationResponse;
import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.budget.api.BudgetResponse;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetModel;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.debts.api.DebtRequest;
import hasanalmunawr.dev.backend_spring.debts.api.DebtResponse;
import hasanalmunawr.dev.backend_spring.debts.model.DebtModel;
import hasanalmunawr.dev.backend_spring.debts.service.DebtService;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import hasanalmunawr.dev.backend_spring.web.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DebtServiceImpl implements DebtService {

    private final GeneralRepository generalRepository;
    private final TaskProcessor taskProcessor;
    private final CurrentUserService currentUserService;

    @Override
    public ResponseEntity<?> createDebt(DebtRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            DebtModel debt = new DebtModel()
                    .setContactName(request.getContactName())
                    .setType(request.getType())
                    .setDueDate(request.getDueDate())
                    .setAmount(request.getAmount())
                    .setIsPaid(request.getIsPaid())
                    .setNotes(request.getNotes())
                    .setUser(currentUser);

            generalRepository.getDebtRepository().save(debt);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, DebtResponse.fromModel(debt));
        });
    }

    @Override
    public ResponseEntity<?> getAllDebts(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            Page<DebtModel> debtData = getDebts(page, size, sort, filter);

            List<DebtResponse> debtContent = debtData.getContent()
                    .stream()
                    .map(DebtResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(debtData.getTotalPages())
                    .setTotalData(debtData.getTotalElements())
                    .setCurrentPage(debtData.getNumber() + 1)
                    .setData(debtContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyDebts(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            Page<DebtModel> debtData = getDebts(currentUser, page, size, sort);

            List<DebtResponse> debtResponse = debtData.getContent()
                    .stream()
                    .map(DebtResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(debtData.getTotalPages())
                    .setTotalData(debtData.getTotalElements())
                    .setCurrentPage(debtData.getNumber() + 1)
                    .setData(debtResponse);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyDebts2(String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            List<DebtResponse> responses = generalRepository.getDebtRepository()
                    .findByUserId(currentUser.getId())
                    .stream()
                    .map(DebtResponse::fromModel)
                    .toList();

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, responses);
        });
    }

    @Override
    public ResponseEntity<?> getDebtById(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            DebtModel budget = generalRepository.getDebtRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, DebtResponse.fromModel(budget));
        });
    }

    @Override
    public ResponseEntity<?> updateDebt(Integer id, DebtRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            DebtModel budget = generalRepository.getDebtRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            updateDebtFromRequest(budget, request);
            generalRepository.getDebtRepository().save(budget);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_UPDATED, null);
        });
    }

    @Override
    public ResponseEntity<?> deleteDebt(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();
            DebtModel debt = generalRepository.getDebtRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));
            debt.setIsDeleted(true);
            debt.setDeletedBy(currentUser.getFullName());
            debt.setDeletedAt(LocalDateTime.now());
            generalRepository.getDebtRepository().save(debt);
            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_DELETED, null);
        });
    }

    private Page<DebtModel> getDebts(int page, int size, String sort ,String filter) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getDebtRepository().searchDebts(filter, pageable);
    }

    private Page<DebtModel> getDebts(UserModel user, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getDebtRepository().findByUserId(user.getId(), pageable);
    }

    private void updateDebtFromRequest(DebtModel debt, DebtRequest request) {
        debt.setContactName(request.getContactName());
        debt.setType(request.getType());
        debt.setAmount(request.getAmount());
        debt.setIsPaid(request.getIsPaid());
        debt.setDueDate(request.getDueDate());
        debt.setNotes(request.getNotes());
    }

}
