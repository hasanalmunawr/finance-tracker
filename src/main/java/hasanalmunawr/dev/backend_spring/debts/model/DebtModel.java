package hasanalmunawr.dev.backend_spring.debts.model;

import hasanalmunawr.dev.backend_spring.base.model.BaseModel;
import hasanalmunawr.dev.backend_spring.category.model.CategoryModel;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import jakarta.persistence.*;
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
@Table(name = "debts")
@Entity
public class DebtModel extends BaseModel {

    @Column(name = "contact_name")
    private String contactName;
    private String type;

    @Column(precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "is_paid")
    private Boolean isPaid;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

}
