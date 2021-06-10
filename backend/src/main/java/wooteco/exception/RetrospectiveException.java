package wooteco.exception;

public class RetrospectiveException extends RuntimeException {

    private final String message;

    public RetrospectiveException(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
