package wooteco.dto.exception;

public class RetrospectiveExceptionResponse {

    private int httpStatus;
    private String message;

    private RetrospectiveExceptionResponse() {
    }

    public RetrospectiveExceptionResponse(int httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
