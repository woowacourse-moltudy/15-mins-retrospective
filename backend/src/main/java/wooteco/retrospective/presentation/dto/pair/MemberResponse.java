package wooteco.retrospective.presentation.dto.pair;

import wooteco.retrospective.application.dto.MemberResponseDto;

public class MemberResponse {

    private Long id;
    private String name;

    private MemberResponse() {}

    public MemberResponse(MemberResponseDto memberResponseDto) {
        this(memberResponseDto.getId(), memberResponseDto.getName());
    }

    public MemberResponse(Long id, String name) {
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
