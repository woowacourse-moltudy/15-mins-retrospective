package wooteco.retrospective.presentation.dto.pair;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;

public class PairRequest {

    private long date;
    private long conferenceTime;

    private PairRequest() {
    }

    public PairRequest(long date, long conferenceTime) {
        this.date = date;
        this.conferenceTime = conferenceTime;
    }

    public LocalDate getDate() {
        return Instant.ofEpochMilli(date)
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public LocalTime getConferenceTime() {
        return Instant.ofEpochMilli(conferenceTime)
                .atZone(ZoneId.systemDefault())
                .toLocalTime();
    }
}
