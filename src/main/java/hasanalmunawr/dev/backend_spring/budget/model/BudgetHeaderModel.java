package hasanalmunawr.dev.backend_spring.budget.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import hasanalmunawr.dev.backend_spring.base.model.BaseModel;
import hasanalmunawr.dev.backend_spring.user.model.UserModel;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Table(name = "budget_headers")
@Entity
public class BudgetHeaderModel extends BaseModel {

    private String name;

    @Column(unique = true)
    private String slug;

    private String period;

    private String notes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    @OneToMany(mappedBy = "budgetHeader", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<BudgetModel> budgets;

}
