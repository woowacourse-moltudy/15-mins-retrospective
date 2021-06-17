package wooteco.retrospective.exception;

public class NotFoundMemberException extends RetrospectiveException {

    private static final String NOT_FOUND_MESSAGE = "해당 멤버를 찾을 수 없습니다.";

    public NotFoundMemberException() {
        super(NOT_FOUND_MESSAGE);
    }
}
