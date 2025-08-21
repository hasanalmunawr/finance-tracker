package hasanalmunawr.dev.backend_spring.base.exception;

import hasanalmunawr.dev.backend_spring.base.api.ApiResponse;
import hasanalmunawr.dev.backend_spring.base.constants.ResponseMessage;
import hasanalmunawr.dev.backend_spring.base.helper.ResponseHelper;
import lombok.extern.slf4j.Slf4j;
import jakarta.validation.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private ResponseHelper responseHelper;


    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse> handleForbiddenException(ForbiddenException ex) {
        ApiResponse apiError = new ApiResponse()
                .setStatus(false)
                .setMessage(ex.getMessage())
                .setResult(null);
        return ResponseEntity.status(ex.getStatus()).body(apiError);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse> handleNotFoundException(NotFoundException ex) {
        ApiResponse apiError = new ApiResponse()
                .setStatus(false)
                .setMessage(ex.getMessage())
                .setResult(null);
        return ResponseEntity.status(ex.getStatus()).body(apiError);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        log.warn("Invalid request: {}", ex.getLocalizedMessage());
        return responseHelper.errorResponse(HttpStatus.BAD_REQUEST, "Invalid request");
    }

    // Handle HTTP request method not supported exception
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<?> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex, WebRequest request) {
        log.warn("Method not allowed: {}", ex.getMethod());
        return responseHelper.errorResponse(HttpStatus.METHOD_NOT_ALLOWED, "Method not allowed");
    }

    // Handle MissingServletRequestParameterException
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<?> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex, WebRequest request) {
        String message = "Missing parameter: " + ex.getParameterName();
        log.warn(message);
        return responseHelper.errorResponse(HttpStatus.BAD_REQUEST, message);
    }

    // Handle constraint violation exceptions
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getConstraintViolations().forEach(cv -> {
            String propertyPath = cv.getPropertyPath().toString();
            String field = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            errors.put(field, cv.getMessage());
        });
        log.warn("Validation error: {}", errors);
        return responseHelper.errorResponse(HttpStatus.BAD_REQUEST, "Validation error", errors);
    }

    // Handle method argument not valid exception
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }
        log.warn("Validation errors: {}", errors);
        return responseHelper.errorResponse(HttpStatus.BAD_REQUEST, "Validation errors", errors);
    }

    // Handle BindException (e.g., for @Valid in GET requests)
    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        log.warn("Validation errors: {}", errors);
        return responseHelper.errorResponse(HttpStatus.BAD_REQUEST, "Validation errors", errors);
    }

    // Handle NoHandlerFoundException
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<?> handleNoHandlerFoundException(
            NoHandlerFoundException ex, WebRequest request) {
        String message = "No handler found for " + ex.getHttpMethod() + " " + ex.getRequestURL();
        log.warn(message);
        return responseHelper.errorResponse(HttpStatus.NOT_FOUND, message);
    }

    // Handle NoResourceFoundException
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<?> handleNoResourceFoundException(
            NoResourceFoundException ex, WebRequest request) {
        String message = "No resource found for " + ex.getResourcePath();
        log.warn(message);
        return responseHelper.errorResponse(HttpStatus.NOT_FOUND, ResponseMessage.Resource.RESOURCE_NOT_FOUND);
    }

    // Handle other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGlobalException(Exception ex, WebRequest request) {
        log.error("An unexpected error occurred", ex);
        return responseHelper.exceptionResponse("An error occurred: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> handleCustomApiException(CustomApiException ex) {
        return responseHelper.exceptionResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<?> handleAuthException(AuthException ex) {
        return responseHelper.exceptionResponse(ex.getMessage(), ex.getStatus());
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
        return responseHelper.exceptionResponse(ex.getMessage(), ex.getStatus());
    }



}
