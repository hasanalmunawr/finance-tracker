package hasanalmunawr.dev.backend_spring.category.api;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class CategoryRequest {

    @NotBlank(message = "category name must not be blank")
    private String name;

    @NotBlank(message = "category type must not be blank")
    private String type;
}
