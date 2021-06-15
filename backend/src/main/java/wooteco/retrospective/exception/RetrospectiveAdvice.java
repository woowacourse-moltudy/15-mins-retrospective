package wooteco.retrospective.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.retrospective.presentation.dto.exception.RetrospectiveExceptionResponse;

import java.util.Objects;

@RestControllerAdvice
public class RetrospectiveAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RetrospectiveExceptionResponse> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        RetrospectiveExceptionResponse response =
                new RetrospectiveExceptionResponse(Objects.requireNonNull(e.getFieldError()).getDefaultMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<String> authException(AuthorizationException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }
}
