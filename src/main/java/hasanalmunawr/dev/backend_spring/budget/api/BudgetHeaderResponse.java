package hasanalmunawr.dev.backend_spring.budget.api;

import hasanalmunawr.dev.backend_spring.budget.model.BudgetHeaderModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

@Data
@Accessors(chain = true)
public class BudgetHeaderResponse {

    private Integer id;
    private String name;
    private String slug;
    private String period;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BudgetHeaderResponse fromModel(BudgetHeaderModel budget) {
        return new BudgetHeaderResponse()
                .setId(budget.getId())
                .setName(budget.getName())
                .setSlug(budget.getSlug())
                .setPeriod(budget.getPeriod())
                .setNotes(budget.getNotes())
                .setCreatedAt(budget.getCreatedAt())
                .setUpdatedAt(budget.getUpdatedAt());
    }

}
