package hasanalmunawr.dev.backend_spring.base.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class UnauthorizedException extends RuntimeException {

    private final HttpStatus status;

    public UnauthorizedException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
