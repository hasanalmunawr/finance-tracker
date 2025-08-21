package hasanalmunawr.dev.backend_spring.bankAccount.api.response;

import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.user.api.response.UserResponse;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BankAccountResponse {

    private Integer id;
    private String name;
    private String number;
    private BigDecimal balance;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BankAccountResponse fromModel(BankAccount bankAccount) {
        return new BankAccountResponse()
                .setId(bankAccount.getId())
                .setName(bankAccount.getName())
                .setNumber(bankAccount.getNumber())
                .setBalance(bankAccount.getBalance())
                .setUser(UserResponse.fromModel(bankAccount.getUser()))
                .setCreatedAt(bankAccount.getCreatedAt())
                .setUpdatedAt(bankAccount.getUpdatedAt());
    }

}
