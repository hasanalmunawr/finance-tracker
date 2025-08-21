package hasanalmunawr.dev.backend_spring.base.task;

import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.exception.CustomApiException;
import hasanalmunawr.dev.backend_spring.base.exception.NotFoundException;
import hasanalmunawr.dev.backend_spring.base.helper.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Slf4j
@Component
public class TaskProcessor {

    @Autowired
    private ResponseHelper responseHelper;

    public TaskProcessorHandler success(String message, Object data) {
        return new TaskProcessorHandler.SuccessResponse(message, data);
    }

    public TaskProcessorHandler successWithHeader(String message, Object data, String headerName, String headerValue) {
        return new TaskProcessorHandler.SuccessResponseWithHeader(message, data, headerName, headerValue);
    }

    public TaskProcessorHandler failed(String message, Object data, HttpStatus status) {
        return new TaskProcessorHandler.ErrorResponse(message, data, status);
    }

    public <T> ResponseEntity<?> executeResponseHttp(Supplier<TaskProcessorHandler> action) {
        try {
            return action.get().handle(responseHelper);
        } catch (CustomApiException ex) {
            return responseHelper.errorResponse(ex.getStatus(), ex.getMessage(), null);
        } catch (IllegalArgumentException ex) {
            return responseHelper.errorResponse(HttpStatus.BAD_REQUEST, ex.getMessage(), null);
        } catch (DisabledException ex) {
            return responseHelper.errorResponse(HttpStatus.UNAUTHORIZED, ResponseMessage.Authentication.ACCOUNT_DISABLED, null);
        } catch (UsernameNotFoundException ex) {
            return responseHelper.errorResponse(HttpStatus.UNAUTHORIZED, ResponseMessage.Authentication.ACCOUNT_DISABLED_OR_NOT_FOUND, null);
        } catch (NotFoundException ex) {
            return responseHelper.errorResponse(HttpStatus.NOT_FOUND, ex.getMessage(), null);
        } catch (BadCredentialsException ex) {
            return responseHelper.errorResponse(HttpStatus.NOT_FOUND, ResponseMessage.Authentication.INVALID_CREDENTIALS, null);
        }
        catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            ex.printStackTrace(); // atau pakai log.error
            return responseHelper.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error occurred", null);
        }
    }

}
