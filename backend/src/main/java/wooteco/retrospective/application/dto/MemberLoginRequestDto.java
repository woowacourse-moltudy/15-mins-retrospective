package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.member.Member;

public class MemberLoginRequestDto {

    private String name;

    private MemberLoginRequestDto() {
    }

    public MemberLoginRequestDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Member toMember() {
        return new Member(null, name);
    }
}
