package wooteco.retrospective.presentation.dto.member;


import wooteco.retrospective.application.dto.MemberDTO;

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
