package hasanalmunawr.dev.backend_spring.transaction.api;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class TransactionRequest {

    @NotBlank(message = "Debt type must not be blank")
    private String type;

    @NotNull(message = "Category ID must not be null")
    private Integer category_id;

    @NotNull(message = "From Bank Account ID must not be null")
    private Integer from_bank_account_id;

    @NotNull(message = "To Bank Account ID must not be null")
    private Integer to_bank_account_id;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    private BigDecimal amount;

    @Size(max = 255, message = "Notes must be at most 255 characters")
    private String notes;

    private LocalDate date;


}
