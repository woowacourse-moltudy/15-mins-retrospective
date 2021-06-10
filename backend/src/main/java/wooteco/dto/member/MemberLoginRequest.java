package wooteco.dto.member;

import wooteco.retrospective.domain.member.Member;

import javax.validation.constraints.NotBlank;

public class MemberLoginRequest {

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    public MemberLoginRequest() {
    }

    public MemberLoginRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Member toMember() {
        return new Member(null, name);
    }

}
