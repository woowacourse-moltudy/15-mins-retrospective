package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.member.Member;

public class MemberLoginResponseDto {

    private String token;

    private MemberLoginResponseDto() {
    }

    public MemberLoginResponseDto(String token) {
        this.token = token;
    }

    public static MemberLoginResponseDto from(Member member) {
        return new MemberLoginResponseDto(member.getName());
    }

    public String getToken() {
        return token;
    }
}
