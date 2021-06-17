package wooteco.retrospective.exception;

public class AlreadyExistTimeException extends RetrospectiveException {

    private static final String ALREADY_EXIST_TIME_MESSAGE = "이미 등록된 시간입니다.";

    public AlreadyExistTimeException() {
        super(ALREADY_EXIST_TIME_MESSAGE);
    }
}