package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.member.Member;

public class MemberDTO {

    private Long id;
    private String name;

    public MemberDTO() {
    }

    public MemberDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public static MemberDTO from(Member member) {
        return new MemberDTO(member.getId(), member.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
