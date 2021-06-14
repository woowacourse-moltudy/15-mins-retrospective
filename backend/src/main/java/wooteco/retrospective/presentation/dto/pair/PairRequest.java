package wooteco.retrospective.presentation.dto.pair;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class PairRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime conferenceTime;

    private PairRequest() {
    }

    public PairRequest(LocalDate date, LocalTime conferenceTime) {
        this.date = date;
        this.conferenceTime = conferenceTime;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getConferenceTime() {
        return conferenceTime;
    }
}
