package hasanalmunawr.dev.backend_spring.budget.service.impl;

import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.helper.SlugHelper;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.budget.api.*;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetHeaderModel;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetModel;
import hasanalmunawr.dev.backend_spring.budget.service.BudgetHeaderService;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import hasanalmunawr.dev.backend_spring.web.service.CurrentUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@RequiredArgsConstructor
public class BudgetHeaderServiceImpl implements BudgetHeaderService {

    private final GeneralRepository generalRepository;
    private final TaskProcessor taskProcessor;
    private final CurrentUserService currentUserService;

    @Override
    public ResponseEntity<?> createBudgetHeader(BudgetHeaderRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            String nameSlug = generateSlug(request.getName());

            BudgetHeaderModel budgetHeaderModel = new BudgetHeaderModel()
                    .setUser(currentUser)
                    .setName(request.getName())
                    .setSlug(nameSlug)
                    .setPeriod(request.getPeriod())
                    .setNotes(request.getNotes());

            generalRepository.getBudgetHeaderRepository().save(budgetHeaderModel);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, BudgetHeaderResponse.fromModel(budgetHeaderModel));
        });
    }

    @Override
    public ResponseEntity<?> getMyBudgetHeaders(String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            List<BudgetHeaderModel> budgetHeaderList = generalRepository.getBudgetHeaderRepository().findByUserId(currentUser.getId());

            List<BudgetHeaderResponse> responses = budgetHeaderList.stream()
                    .map(BudgetHeaderResponse::fromModel)
                    .toList();

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, responses);
        });
    }

    @Override
    public ResponseEntity<?> getBudgetById(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            BudgetHeaderModel budgetHeaderModel = generalRepository.getBudgetHeaderRepository().findById(id)
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, BudgetHeaderWithListResponse.fromModel(budgetHeaderModel));
        });
    }

    @Override
    public ResponseEntity<?> updateBudgetHeader(Integer id, BudgetHeaderRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            BudgetHeaderModel budgetHeaderModel = generalRepository.getBudgetHeaderRepository().findById(id)
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            updateBudgetHeaderFromRequest(budgetHeaderModel, request);
            generalRepository.getBudgetHeaderRepository().save(budgetHeaderModel);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_UPDATED, null);
        });
    }

    @Override
    public ResponseEntity<?> deleteBudgetHeader(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            BudgetHeaderModel budgetHeaderModel = generalRepository.getBudgetHeaderRepository().findById(id)
                    .orElseThrow(() -> new RuntimeException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            budgetHeaderModel.setIsDeleted(true);
            budgetHeaderModel.setDeletedBy(currentUser.getFullName());
            budgetHeaderModel.setDeletedAt(LocalDateTime.now());
            generalRepository.getBudgetHeaderRepository().save(budgetHeaderModel);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_DELETED, null);
        });
    }

    private void updateBudgetHeaderFromRequest(BudgetHeaderModel budgetHeader, BudgetHeaderRequest request) {
        budgetHeader.setName(request.getName());
        budgetHeader.setPeriod(request.getPeriod());
        budgetHeader.setNotes(request.getNotes());
    }

    private String generateSlug(String title) {
        String baseSlug = SlugHelper.toSlug(title);
        String slug = baseSlug;
        int counter = 1;

        while (generalRepository.getBudgetHeaderRepository().findBySlug(slug).isPresent()) {
            slug = baseSlug + "-" + counter;
            counter++;
        }
        return slug;
    }
}
