package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.pair.Pair;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PairResponseDto {

    private List<MemberResponseDto> members;

    private PairResponseDto() {}

    public PairResponseDto(Pair pair) {
        this.members = pair.getMembers().stream()
                .map(MemberResponseDto::new)
                .collect(toList());
    }

    public PairResponseDto(List<MemberResponseDto> memberResponseDtos) {
        this.members = memberResponseDtos;
    }

    public List<MemberResponseDto> getMembers() {
        return members;
    }
}
