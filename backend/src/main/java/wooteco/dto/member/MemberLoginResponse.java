package wooteco.dto.member;

import wooteco.retrospective.domain.member.Member;

public class MemberLoginResponse {

    private String token;

    public MemberLoginResponse() {
    }

    public MemberLoginResponse(String token) {
        this.token = token;
    }

    public static MemberLoginResponse of(Member member) {
        return new MemberLoginResponse(member.getName());
    }

    public String getToken() {
        return token;
    }

}
