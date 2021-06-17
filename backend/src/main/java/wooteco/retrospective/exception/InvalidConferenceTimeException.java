package wooteco.retrospective.exception;

public class InvalidConferenceTimeException extends InvalidTimeException {

    private static final String MESSAGE = "잘못된 회고 시간 입니다.";

    public InvalidConferenceTimeException() {
        this(MESSAGE);
    }

    public InvalidConferenceTimeException(String message) {
        super(message);
    }
}
