package hasanalmunawr.dev.backend_spring.base.helper;

import hasanalmunawr.dev.backend_spring.base.api.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseHelper {

    public ResponseEntity<?> successResponse(String message, Object result) {
        return ResponseEntity.ok(new ApiResponse().setStatus(true).setMessage(message).setResult(result));
    }

    public ResponseEntity<?> successResponseWithHeader(String message, Object result, String headerName, String headerValue) {
        return ResponseEntity.ok()
                .header(headerName, headerValue)
                .body(new ApiResponse().setStatus(true).setMessage(message).setResult(result));
    }

    public ResponseEntity<?> successResponseWithCookie(String message, Object result, HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        var cookie = new jakarta.servlet.http.Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);

        return ResponseEntity.ok(new ApiResponse().setStatus(true).setMessage(message).setResult(result));
    }

    public ResponseEntity<?> errorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse().setStatus(false).setMessage(message).setResult(null));
    }

    public ResponseEntity<?> errorResponse(HttpStatus status, String message, Object result) {
        return ResponseEntity.status(status)
                .body(new ApiResponse().setStatus(false).setMessage(message).setResult(result));
    }

    public ResponseEntity<?> exceptionResponse(String message, HttpStatus statusException) {
        return new ResponseEntity<>(new ApiResponse().setStatus(false).setMessage(message).setResult(null), statusException);
    }

}
