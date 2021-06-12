package wooteco.dto.exception;

public class RetrospectiveExceptionResponse {

    private String message;

    private RetrospectiveExceptionResponse() {
    }

    public RetrospectiveExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
