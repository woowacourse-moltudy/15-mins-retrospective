package wooteco.retrospective.exception;

public class NotFoundTimeException extends RetrospectiveException {

    private static final String NOT_FOUND_MESSAGE = "해당 시간을 찾을 수 없습니다.";

    public NotFoundTimeException() {
        super(NOT_FOUND_MESSAGE);
    }
}

