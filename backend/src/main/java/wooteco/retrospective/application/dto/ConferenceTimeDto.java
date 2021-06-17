package wooteco.retrospective.application.dto;

import java.time.LocalTime;

public class ConferenceTimeDto {
    private final Long id;
    private final LocalTime conferenceTime;

    public ConferenceTimeDto(Long id, LocalTime conferenceTime) {
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
