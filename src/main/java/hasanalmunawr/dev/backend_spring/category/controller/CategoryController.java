package hasanalmunawr.dev.backend_spring.category.controller;

import hasanalmunawr.dev.backend_spring.bankAccount.api.request.BankAccountRequest;
import hasanalmunawr.dev.backend_spring.base.constants.Endpoint;
import hasanalmunawr.dev.backend_spring.category.api.CategoryRequest;
import hasanalmunawr.dev.backend_spring.category.service.CategoryService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static hasanalmunawr.dev.backend_spring.sales.api.swagger.ServiceOrderExample.SERVICE_ORDER_CREATE_RESPONSE_SUCCESS;
import static hasanalmunawr.dev.backend_spring.sales.api.swagger.ServiceOrderExample.SERVICE_ORDER_REQUEST_JSON;

@RestController
@RequestMapping(Endpoint.Base.CATEGORY)
@Tag(name = "Category Controller", description = "API for managing service-order-related operations.")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping()
    public ResponseEntity<?> getAllBankAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return categoryService.getAllCategories(page, size, sort, filter);
    }

    @GetMapping("/mine")
    public ResponseEntity<?> getMyBankAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return categoryService.getMyCategories(page, size, sort, filter);
    }

    @GetMapping("/mine-2")
    public ResponseEntity<?> getMyCategories2(
            @RequestParam(defaultValue = "asc") String sort,
            @RequestParam(required = false) String filter
    ) {
        return categoryService.getMyCategories2(sort, filter);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBankAccountById(@PathVariable Integer id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping(Endpoint.Basic.CREATE)
    public ResponseEntity<?> createBankAccount(
            @Valid @RequestBody CategoryRequest request
    ) {
        return categoryService.createCategory(request);
    }

    @PutMapping(Endpoint.Basic.UPDATE + "/{id}")
    public ResponseEntity<?> updateBankAccount(@PathVariable Integer id, @Valid @RequestBody CategoryRequest request) {
        return categoryService.updateCategory(id, request);
    }

    @DeleteMapping(Endpoint.Basic.DELETE + "/{id}")
    public ResponseEntity<?> deleteBankAccount(@PathVariable Integer id) {
        return categoryService.deleteCategory(id);
    }
}
