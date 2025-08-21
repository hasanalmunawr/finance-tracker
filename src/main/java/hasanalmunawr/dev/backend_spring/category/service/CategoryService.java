package hasanalmunawr.dev.backend_spring.category.service;

import hasanalmunawr.dev.backend_spring.bankAccount.api.request.BankAccountRequest;
import hasanalmunawr.dev.backend_spring.category.api.CategoryRequest;
import org.springframework.http.ResponseEntity;

public interface CategoryService {

    ResponseEntity<?> createCategory(CategoryRequest request);
    ResponseEntity<?> getAllCategories(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyCategories(int page, int size, String sort, String filter);
    ResponseEntity<?> getMyCategories2(String sort, String filter);
    ResponseEntity<?> getCategoryById(Integer id);
    ResponseEntity<?> updateCategory(Integer id, CategoryRequest request);
    ResponseEntity<?> deleteCategory(Integer id);

}
