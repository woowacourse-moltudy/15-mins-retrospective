package wooteco.retrospective.presentation.dto.pair;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public class PairRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private Long conferenceTimeId;

    private PairRequest() {
    }

    public PairRequest(LocalDate date, Long conferenceTimeId) {
        this.date = date;
        this.conferenceTimeId = conferenceTimeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getConferenceTimeId() {
        return conferenceTimeId;
    }
}
