package hasanalmunawr.dev.backend_spring.budget.service.impl;

import hasanalmunawr.dev.backend_spring.base.api.PaginationResponse;
import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.budget.api.BudgetRequest;
import hasanalmunawr.dev.backend_spring.budget.api.BudgetResponse;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetHeaderModel;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetModel;
import hasanalmunawr.dev.backend_spring.budget.service.BudgetService;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
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
public class BudgetServiceImpl implements BudgetService {

    private final GeneralRepository generalRepository;
    private final TaskProcessor taskProcessor;
    private final CurrentUserService currentUserService;

    @Override
    public ResponseEntity<?> createBudget(BudgetRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            CategoryModel categoryModel = generalRepository.getCategoryRepository().findById(request.getCategory_id())
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            BudgetHeaderModel budgetHeaderModel = generalRepository.getBudgetHeaderRepository().findById(request.getBudget_header_id())
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            BudgetModel budget = new BudgetModel()
                    .setDescription(request.getDescription())
                    .setAmount(request.getAmount())
                    .setCategory(categoryModel)
                    .setBudgetHeader(budgetHeaderModel)
                    .setUser(currentUser);

            generalRepository.getBudgetRepository().save(budget);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, BudgetResponse.fromModel(budget));
        });
    }

    @Override
    public ResponseEntity<?> getAllBudgets(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            Page<BudgetModel> budgetData = getBudgets(page, size, sort, filter);

            List<BudgetResponse> budgetContent = budgetData.getContent()
                    .stream()
                    .map(BudgetResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(budgetData.getTotalPages())
                    .setTotalData(budgetData.getTotalElements())
                    .setCurrentPage(budgetData.getNumber() + 1)
                    .setData(budgetContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyBudgets(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            Page<BudgetModel> budgetModal = getMyBudgets(currentUser, page, size, sort);

            List<BudgetResponse> budgetContent = budgetModal.getContent()
                    .stream()
                    .map(BudgetResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(budgetModal.getTotalPages())
                    .setTotalData(budgetModal.getTotalElements())
                    .setCurrentPage(budgetModal.getNumber() + 1)
                    .setData(budgetContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyBudgets2(String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            List<BudgetResponse> responses = generalRepository.getBudgetRepository()
                    .findByUserId(currentUser.getId())
                    .stream()
                    .map(BudgetResponse::fromModel)
                    .toList();

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, responses);
        });
    }

    @Override
    public ResponseEntity<?> getBudgetById(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            BudgetModel budget = generalRepository.getBudgetRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, BudgetResponse.fromModel(budget));
        });
    }

     @Override
    public ResponseEntity<?> getBudgetByHeaderId(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            List<BudgetModel> budgetModels = generalRepository.getBudgetRepository().findByBudgetHeaderId(currentUser.getId(), id);

            List<BudgetResponse> responses = budgetModels.stream()
                    .map(BudgetResponse::fromModel)
                    .toList();

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, responses);
        });
    }



    @Override
    public ResponseEntity<?> updateBudget(Integer id, BudgetRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            BudgetModel budget = generalRepository.getBudgetRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            updateBudgetFromRequest(budget, request);
            generalRepository.getBudgetRepository().save(budget);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_UPDATED, null);
        });
    }

    @Override
    public ResponseEntity<?> deleteBudget(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();
            BudgetModel budget = generalRepository.getBudgetRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));
            budget.setIsDeleted(true);
            budget.setDeletedBy(currentUser.getFullName());
            budget.setDeletedAt(LocalDateTime.now());
            generalRepository.getBudgetRepository().save(budget);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_DELETED, null);
        });
    }

    private Page<BudgetModel> getBudgets(int page, int size, String sort ,String filter) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getBudgetRepository().searchBudget(filter, pageable);
    }

    private Page<BudgetModel> getMyBudgets(UserModel user, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getBudgetRepository().findByUserId(user.getId(), pageable);
    }

    private void updateBudgetFromRequest(BudgetModel budget, BudgetRequest request) {
        if (request.getCategory_id() != null) {
            CategoryModel categoryModel = generalRepository.getCategoryRepository().findById(request.getCategory_id())
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));
            budget.setCategory(categoryModel);
        }
        budget.setDescription(request.getDescription());
        budget.setAmount(request.getAmount());
    }
}
