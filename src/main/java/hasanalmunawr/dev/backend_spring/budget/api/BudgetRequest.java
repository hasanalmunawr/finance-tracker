package hasanalmunawr.dev.backend_spring.budget.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class BudgetRequest {

    @NotNull
    private Integer category_id;
    @NotNull
    private Integer budget_header_id;
    @NotBlank(message = "Budget description must not be blank")
    private String description;
    @NonNull()
    private BigDecimal amount;

}
