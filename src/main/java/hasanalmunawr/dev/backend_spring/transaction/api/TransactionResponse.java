package hasanalmunawr.dev.backend_spring.transaction.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.debts.api.DebtResponse;
import hasanalmunawr.dev.backend_spring.debts.model.DebtModel;
import hasanalmunawr.dev.backend_spring.transaction.model.TransactionModel;
import hasanalmunawr.dev.backend_spring.user.api.response.UserResponse;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class TransactionResponse {

    private Integer id;
    private String type;
    private BigDecimal amount;
    private LocalDate date;
    private Integer from_bank_account_id;
    private Integer to_bank_account_id;
    private String from_bank_account_name;
    private String to_bank_account_name;
    private String notes;
    private String category;
    private UserResponse user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TransactionResponse fromModel(TransactionModel request) {
        return new TransactionResponse()
                .setId(request.getId())
                .setType(request.getType())
                .setAmount(request.getAmount())
                .setFrom_bank_account_id(request.getFromBankAccount().getId())
                .setTo_bank_account_id(request.getToBankAccount().getId())
                .setFrom_bank_account_name(request.getFromBankAccount().getName())
                .setTo_bank_account_name(request.getToBankAccount().getName())
                .setDate(request.getDate())
                .setNotes(request.getNotes())
                .setCategory(request.getCategory().getName())
                .setUser(UserResponse.fromModel(request.getUser()))
                .setCreatedAt(request.getCreatedAt())
                .setUpdatedAt(request.getUpdatedAt());
    }

}
