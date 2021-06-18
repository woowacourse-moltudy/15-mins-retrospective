package wooteco.retrospective.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@RestControllerAdvice
public class RetrospectiveAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        String message = Objects.requireNonNull(e.getFieldError()).getDefaultMessage();
        return ResponseEntity.badRequest()
                .body(
                        new ErrorDto(message)
                );
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorDto> invalidTokenException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        new ErrorDto(e.getMessage())
                );
    }

    @ExceptionHandler(RetrospectiveException.class)
    public ResponseEntity<ErrorDto> retrospectiveException(RetrospectiveException e) {
        return ResponseEntity.badRequest()
                .body(
                        new ErrorDto(e.getMessage())
                );
    }

    static class ErrorDto {

        private String message;

        private ErrorDto() {}

        public ErrorDto(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
