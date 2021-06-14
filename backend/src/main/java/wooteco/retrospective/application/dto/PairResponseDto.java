package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.pair.Pair;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PairResponseDto {

    private List<MemberResponseDto> pair;

    private PairResponseDto() {}

    public PairResponseDto(Pair pair) {
        this.pair = pair.getAttendances().stream()
                .map(Attendance::getMember)
                .map(MemberResponseDto::new)
                .collect(toList());
    }

    public PairResponseDto(List<MemberResponseDto> memberResponseDtos) {
        this.pair = memberResponseDtos;
    }

    public List<MemberResponseDto> getPair() {
        return pair;
    }
}
