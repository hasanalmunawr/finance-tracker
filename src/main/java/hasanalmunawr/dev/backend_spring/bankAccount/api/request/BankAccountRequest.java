package hasanalmunawr.dev.backend_spring.bankAccount.api.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class BankAccountRequest {

    @NotBlank(message = "bank account name must not be blank")
    private String name;

    @NotBlank(message = "bank account number must not be blank")
    private String number;

    @NonNull()
    private BigDecimal balance;

}
