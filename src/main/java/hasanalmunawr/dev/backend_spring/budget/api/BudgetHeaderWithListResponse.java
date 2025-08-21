package hasanalmunawr.dev.backend_spring.budget.api;

import hasanalmunawr.dev.backend_spring.budget.model.BudgetHeaderModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Accessors(chain = true)
public class BudgetHeaderWithListResponse {

    private Integer id;
    private String name;
    private String slug;
    private String period;
    private String notes;
    private List<BudgetResponse> budgets;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static BudgetHeaderWithListResponse fromModel(BudgetHeaderModel budget) {
        return new BudgetHeaderWithListResponse()
                .setId(budget.getId())
                .setName(budget.getName())
                .setSlug(budget.getSlug())
                .setPeriod(budget.getPeriod())
                .setNotes(budget.getNotes())
                .setBudgets(
                        budget.getBudgets()
                                .stream()
                                .map(BudgetResponse::fromModel)
                                .toList()
                )
                .setCreatedAt(budget.getCreatedAt())
                .setUpdatedAt(budget.getUpdatedAt());
    }
}
