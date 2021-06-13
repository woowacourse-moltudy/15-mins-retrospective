package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.member.Member;

public class MemberLoginDto {

    private String name;

    private MemberLoginDto() {
    }

    public MemberLoginDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Member toMember() {
        return new Member(null, name);
    }
}
