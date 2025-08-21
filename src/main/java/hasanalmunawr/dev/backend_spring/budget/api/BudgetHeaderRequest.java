package hasanalmunawr.dev.backend_spring.budget.api;

import lombok.Data;

@Data
public class BudgetHeaderRequest {

    private String name;
    private String period;
    private String notes;
}
