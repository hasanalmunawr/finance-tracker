package hasanalmunawr.dev.backend_spring.category.service.impl;

import hasanalmunawr.dev.backend_spring.bankAccount.api.request.BankAccountRequest;
import hasanalmunawr.dev.backend_spring.bankAccount.api.response.BankAccountResponse;
import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.base.api.PaginationResponse;
import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.repository.GeneralRepository;
import hasanalmunawr.dev.backend_spring.base.task.TaskProcessor;
import hasanalmunawr.dev.backend_spring.category.api.CategoryRequest;
import hasanalmunawr.dev.backend_spring.category.api.CategoryResponse;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.category.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {

    private final GeneralRepository generalRepository;
    private final TaskProcessor taskProcessor;
    private final CurrentUserService currentUserService;

    @Override
    public ResponseEntity<?> createCategory(CategoryRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            CategoryModel category = new CategoryModel()
                    .setName(request.getName())
                    .setType(request.getType())
                    .setUser(currentUser);

            generalRepository.getCategoryRepository().save(category);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, CategoryResponse.fromModel(category));
        });
    }

    @Override
    public ResponseEntity<?> getAllCategories(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            Page<CategoryModel> categoryModel = getCategories(page, size, sort, filter);

            List<CategoryResponse> serviceOrderContent = categoryModel.getContent()
                    .stream()
                    .map(CategoryResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(categoryModel.getTotalPages())
                    .setTotalData(categoryModel.getTotalElements())
                    .setCurrentPage(categoryModel.getNumber() + 1)
                    .setData(serviceOrderContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyCategories(int page, int size, String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();

            Page<CategoryModel> categoryModel = getMyCategories(currentUser, page, size, sort);

            List<CategoryResponse> serviceOrderContent = categoryModel.getContent()
                    .stream()
                    .map(CategoryResponse::fromModel)
                    .toList();

            PaginationResponse response = new PaginationResponse()
                    .setPage(page)
                    .setSize(size)
                    .setTotalPage(categoryModel.getTotalPages())
                    .setTotalData(categoryModel.getTotalElements())
                    .setCurrentPage(categoryModel.getNumber() + 1)
                    .setData(serviceOrderContent);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, response);
        });
    }

    @Override
    public ResponseEntity<?> getMyCategories2(String sort, String filter) {
        return taskProcessor.executeResponseHttp(() -> {

            UserModel currentUser = currentUserService.getCurrentUser();

            List<CategoryModel> categoryModels = generalRepository.getCategoryRepository()
                    .findByUserId(currentUser.getId());

            List<CategoryResponse> responses = categoryModels.stream()
                    .map(CategoryResponse::fromModel)
                    .toList();

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_FOUND, responses);
        });
    }

    @Override
    public ResponseEntity<?> getCategoryById(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {

            CategoryModel categoryModel = generalRepository.getCategoryRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_CREATED, CategoryResponse.fromModel(categoryModel));
        });
    }

    @Override
    public ResponseEntity<?> updateCategory(Integer id, CategoryRequest request) {
        return taskProcessor.executeResponseHttp(() -> {

            CategoryModel category = generalRepository.getCategoryRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));

            updateCategoryFromRequest(category, request);
            generalRepository.getCategoryRepository().save(category);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_UPDATED, null);
        });
    }

    @Override
    public ResponseEntity<?> deleteCategory(Integer id) {
        return taskProcessor.executeResponseHttp(() -> {
            UserModel currentUser = currentUserService.getCurrentUser();
            CategoryModel category = generalRepository.getCategoryRepository().findById(id)
                    .orElseThrow(() -> new NotFoundException(ResponseMessage.Resource.RESOURCE_NOT_FOUND));
            category.setIsDeleted(true);
            category.setDeletedBy(currentUser.getFullName());
            category.setDeletedAt(LocalDateTime.now());
            generalRepository.getCategoryRepository().save(category);

            return taskProcessor.success(ResponseMessage.Resource.RESOURCE_DELETED, null);
        });
    }

    private Page<CategoryModel> getCategories(int page, int size, String sort ,String filter) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getCategoryRepository().searchCategory(filter, pageable);
    }

    private Page<CategoryModel> getMyCategories(UserModel user, int page, int size, String sort) {
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sort.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC, "created_at")
        );
        return generalRepository.getCategoryRepository().findByUserId(user.getId(), pageable);
    }

    private void updateCategoryFromRequest(CategoryModel category, CategoryRequest request) {
        category.setName(request.getName());
        category.setType(request.getType());
    }
}
