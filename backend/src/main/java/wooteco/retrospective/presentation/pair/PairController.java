package wooteco.retrospective.presentation.pair;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.application.pair.PairService;
import wooteco.retrospective.presentation.dto.pair.PairRequest;
import wooteco.retrospective.presentation.dto.pair.PairResponse;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RequestMapping("/api/pairs")
@RestController
public class PairController {

    private final PairService pairService;

    public PairController(PairService pairService) {
        this.pairService = pairService;
    }

    @GetMapping
    public ResponseEntity<List<PairResponse>> getPairs(@ModelAttribute PairRequest pairRequest) {
        List<PairResponseDto> pairs = pairService.getPairsByDateAndTime(
                pairRequest.getDate(),
                pairRequest.getConferenceTimeId(),
                LocalDateTime.now()
        );

        return ResponseEntity.ok(pairs.stream()
                .map(PairResponse::new)
                .collect(toList()));
    }
}
