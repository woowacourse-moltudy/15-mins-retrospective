package wooteco.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import wooteco.dto.exception.RetrospectiveExceptionResponse;

@RestControllerAdvice
public class RetrospectiveAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<RetrospectiveExceptionResponse> notFoundException(NotFoundException e) {
        int httpStatus = HttpStatus.NOT_FOUND.value();
        RetrospectiveExceptionResponse response =
                new RetrospectiveExceptionResponse(httpStatus, e.getMessage());
        return ResponseEntity.status(httpStatus).body(response);
    }

}
