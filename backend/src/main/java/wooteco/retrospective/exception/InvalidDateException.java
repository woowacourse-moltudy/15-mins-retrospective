package wooteco.retrospective.exception;

public class InvalidDateException extends RetrospectiveException {

    private final static String MESSAGE = "잘못된 날짜 입니다.";

    public InvalidDateException() {
        this(MESSAGE);
    }

    public InvalidDateException(String message) {
        super(message);
    }
}
