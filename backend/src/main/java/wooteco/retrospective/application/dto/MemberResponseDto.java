package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.member.Member;

public class MemberResponseDto {

    private Long id;
    private String name;

    private MemberResponseDto() {}

    public MemberResponseDto(Member member) {
        this(member.getId(), member.getName());
    }

    public MemberResponseDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
