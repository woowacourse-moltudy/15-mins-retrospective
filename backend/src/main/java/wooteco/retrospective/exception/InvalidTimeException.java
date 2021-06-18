package wooteco.retrospective.exception;

public class InvalidTimeException extends RetrospectiveException {

    private static final String MESSAGE = "잘못된 시간 입니다.";

    public InvalidTimeException() {
        this(MESSAGE);
    }

    public InvalidTimeException(String message) {
        super(message);
    }
}
