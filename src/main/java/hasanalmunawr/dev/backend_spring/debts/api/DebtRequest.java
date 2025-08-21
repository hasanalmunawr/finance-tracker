package hasanalmunawr.dev.backend_spring.debts.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DebtRequest {

//    @NotNull(message = "Category ID must not be null")
//    private Integer category_id;

    @NotBlank(message = "Debt Contact Name must not be blank")
    private String contactName;

    @NotBlank(message = "Debt type must not be blank")
    private String type;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @FutureOrPresent(message = "Due date must be today or in the future")
    @JsonProperty("due_date")
    private LocalDate dueDate;

    @JsonProperty("is_paid")
    private Boolean isPaid;

    @Size(max = 255, message = "Notes must be at most 255 characters")
    private String notes;

}
