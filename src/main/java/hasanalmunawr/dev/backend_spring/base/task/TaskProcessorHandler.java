package hasanalmunawr.dev.backend_spring.base.task;

import hasanalmunawr.dev.backend_spring.base.helper.ResponseHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public sealed interface TaskProcessorHandler {

    String message();
    Object data();
    HttpStatus httpStatus();

    default ResponseEntity<?> handle(ResponseHelper responseHelper) {
        try {
            if (this instanceof SuccessResponse success) {
                return responseHelper.successResponse(success.message(), success.data());
            }
            else if (this instanceof SuccessResponseWithHeader success) {
                return responseHelper.successResponseWithHeader(success.message(), success.data(), success.headerName(), success.headerValue());
            }
            else if (this instanceof ErrorResponse error) {
                return responseHelper.errorResponse(error.httpStatus(), error.message(), error.data());
            }
            throw new UnsupportedOperationException("Unknown Task Processor Data implementation");
        } catch (Exception e) {
            return responseHelper.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    record SuccessResponse(String message, Object data) implements TaskProcessorHandler {

        @Override
        public HttpStatus httpStatus() {
            return HttpStatus.OK;
        }
    }

    record SuccessResponseWithHeader(String message, Object data, String headerName, String headerValue) implements TaskProcessorHandler {

        @Override
        public HttpStatus httpStatus() {
            return HttpStatus.OK;
        }
    }

    record ErrorResponse(String message, Object data, HttpStatus httpStatus) implements TaskProcessorHandler {}
}
