package hasanalmunawr.dev.backend_spring.budget.api;

import hasanalmunawr.dev.backend_spring.base.model.BaseModel;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetModel;
import hasanalmunawr.dev.backend_spring.category.api.CategoryResponse;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.user.api.response.UserResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BudgetResponse {

    private Integer id;
    private String description;
    private BigDecimal amount;
    private Integer categoryId;
    private CategoryResponse category;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BudgetResponse fromModel(BudgetModel budget) {
        return new BudgetResponse()
                .setId(budget.getId())
                .setDescription(budget.getDescription())
                .setAmount(budget.getAmount())
                .setCategoryId(budget.getCategory().getId())
                .setCategory(CategoryResponse.fromModel(budget.getCategory()))
                .setUser(UserResponse.fromModel(budget.getUser()))
                .setCreatedAt(budget.getCreatedAt())
                .setUpdatedAt(budget.getUpdatedAt());
    }
}
