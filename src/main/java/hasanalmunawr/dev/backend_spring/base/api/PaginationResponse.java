package hasanalmunawr.dev.backend_spring.base.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PaginationResponse {
    private Integer page;
    private Integer size;
    private Integer totalPage;
    private Long totalData;
    private Integer currentPage;
    private Object data;
}
