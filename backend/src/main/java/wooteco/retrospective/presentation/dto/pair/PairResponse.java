package wooteco.retrospective.presentation.dto.pair;

import wooteco.retrospective.application.dto.MemberResponseDto;
import wooteco.retrospective.application.dto.PairResponseDto;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class PairResponse {

    private List<MemberResponse> pair;

    private PairResponse() {
    }

    public PairResponse(PairResponseDto pairResponseDto) {
        this.pair = pairResponseDto.getPair().stream()
                .map(MemberResponse::new)
                .collect(toList());
    }

    public List<MemberResponse> getPair() {
        return pair;
    }
}
