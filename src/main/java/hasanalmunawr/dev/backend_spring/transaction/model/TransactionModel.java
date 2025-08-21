package hasanalmunawr.dev.backend_spring.transaction.model;

import hasanalmunawr.dev.backend_spring.bankAccount.model.BankAccount;
import hasanalmunawr.dev.backend_spring.bankAccount.repository.BankAccountRepository;
import hasanalmunawr.dev.backend_spring.base.model.BaseModel;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(name = "transactions")
@Entity
public class TransactionModel extends BaseModel {

    private String type;

//    @Column(name = "from_bank_account_id", nullable = false)
//    private Integer from_bank_account_id;
//
//    @Column(name = "to_bank_account_id")
//    private Integer to_bank_account_id;

    @Column(precision = 18, scale = 2)
    private BigDecimal amount;

    private String notes;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryModel category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_bank_account_id", updatable = false)
    private BankAccount fromBankAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_bank_account_id", updatable = false)
    private BankAccount toBankAccount;




}
