package hasanalmunawr.dev.backend_spring.category.api;

import hasanalmunawr.dev.backend_spring.bankAccount.api.response.BankAccountResponse;
import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.user.api.response.UserResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class CategoryResponse {

    private Integer id;
    private String name;
    private String type;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static CategoryResponse fromModel(CategoryModel category) {
        return new CategoryResponse()
                .setId(category.getId())
                .setName(category.getName())
                .setType(category.getType())
                .setUser(UserResponse.fromModel(category.getUser()))
                .setCreatedAt(category.getCreatedAt())
                .setUpdatedAt(category.getUpdatedAt());
    }
}
