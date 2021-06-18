package wooteco.retrospective.presentation.dto;

import java.time.LocalTime;

public class TimeResponse {

    private final Long id;
    private final LocalTime conferenceTime;

    public TimeResponse(Long id, LocalTime conferenceTime) {
        this.id = id;
        this.conferenceTime = conferenceTime;
    }

    public Long getId() {
        return id;
    }

    public LocalTime getConferenceTime() {
        return conferenceTime;
    }
}
