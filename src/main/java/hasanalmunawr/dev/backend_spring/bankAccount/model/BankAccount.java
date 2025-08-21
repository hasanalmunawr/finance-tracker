package hasanalmunawr.dev.backend_spring.bankAccount.model;

import hasanalmunawr.dev.backend_spring.base.model.BaseModel;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@Setter
@Getter
@Accessors(chain = true)
@Table(name = "bank_accounts")
@Entity
public class BankAccount extends BaseModel {

    private String name;
    private String number;

    @Column(precision = 18, scale = 2)
    private BigDecimal balance;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;
}
