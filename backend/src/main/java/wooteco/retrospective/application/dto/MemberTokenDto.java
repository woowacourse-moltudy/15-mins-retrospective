package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.member.Member;

public class MemberTokenDto {

    private String token;

    private MemberTokenDto() {
    }

    public MemberTokenDto(String token) {
        this.token = token;
    }

    public static MemberTokenDto from(String token) {
        return new MemberTokenDto(token);
    }

    public String getToken() {
        return token;
    }
}
