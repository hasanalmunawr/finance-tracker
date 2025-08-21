package hasanalmunawr.dev.backend_spring.debts.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import hasanalmunawr.dev.backend_spring.budget.api.BudgetResponse;
import hasanalmunawr.dev.backend_spring.budget.model.BudgetModel;
import hasanalmunawr.dev.backend_spring.category.api.CategoryResponse;
import hasanalmunawr.dev.backend_spring.debts.model.DebtModel;
import hasanalmunawr.dev.backend_spring.user.api.response.UserResponse;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class DebtResponse {

    private Integer id;
    private String contactName;
    private String type;
    private BigDecimal amount;
    private LocalDate dueDate;
    private Boolean isPaid;
    private String notes;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static DebtResponse fromModel(DebtModel debt) {
        return new DebtResponse()
                .setId(debt.getId())
                .setContactName(debt.getContactName())
                .setType(debt.getType())
                .setAmount(debt.getAmount())
                .setDueDate(debt.getDueDate())
                .setIsPaid(debt.getIsPaid())
                .setNotes(debt.getNotes())
                .setUser(UserResponse.fromModel(debt.getUser()))
                .setCreatedAt(debt.getCreatedAt())
                .setUpdatedAt(debt.getUpdatedAt());
    }

}
