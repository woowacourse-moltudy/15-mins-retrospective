package wooteco.exception.member;

import wooteco.exception.NotFoundException;

public class MemberNotFoundException extends NotFoundException {

    public MemberNotFoundException() {
        super("해당 회원을 찾을 수 없습니다.");
    }
}
