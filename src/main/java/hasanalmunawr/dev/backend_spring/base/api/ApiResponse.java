package hasanalmunawr.dev.backend_spring.base.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ApiResponse {
    private String message;
    private Object result;
    private Boolean status;
}
