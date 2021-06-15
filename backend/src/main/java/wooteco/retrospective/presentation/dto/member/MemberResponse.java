package wooteco.retrospective.presentation.dto.member;


import wooteco.retrospective.domain.member.Member;

public class MemberResponse {

    private Long id;
    private String name;

    public MemberResponse() {
    }

    public MemberResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MemberResponse from(Member member) {
        return new MemberResponse(member.getId(), member.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
