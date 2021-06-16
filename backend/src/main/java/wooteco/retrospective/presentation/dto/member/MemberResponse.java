package wooteco.retrospective.presentation.dto.member;


import wooteco.retrospective.domain.dto.MemberDTO;
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

    public static MemberResponse from(MemberDTO memberDTO) {
        return new MemberResponse(memberDTO.getId(), memberDTO.getName());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
