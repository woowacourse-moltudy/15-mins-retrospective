package wooteco.dto.member;

import wooteco.retrospective.domain.member.Member;

public class MemberLoginResponse {

    private String token;

    private MemberLoginResponse() {
    }

    public MemberLoginResponse(String token) {
        this.token = token;
    }

    public static MemberLoginResponse from(Member member) {
        return new MemberLoginResponse(member.getName());
    }

    public String getToken() {
        return token;
    }
}
