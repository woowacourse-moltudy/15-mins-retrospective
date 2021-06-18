package wooteco.retrospective.domain.attendance;

import java.time.LocalTime;
import java.util.Objects;

import wooteco.retrospective.application.dto.ConferenceTimeDto;

public class ConferenceTime {

    private final Long id;
    private final LocalTime conferenceTime;

    public ConferenceTime(LocalTime conferenceTime) {
        this(null, conferenceTime);
    }

    public ConferenceTime(Long id, LocalTime conferenceTime) {
        this.id = id;
        this.conferenceTime = conferenceTime;
    }

    public static ConferenceTime of(ConferenceTimeDto conferenceTimeDto) {
        return new ConferenceTime(conferenceTimeDto.getId(), conferenceTimeDto.getConferenceTime());
    }

    public boolean isBefore(ConferenceTime conferenceTime) {
        return this.conferenceTime.isBefore(conferenceTime.getConferenceTime());
    }

    public long getId() {
        return id;
    }

    public LocalTime getConferenceTime() {
        return conferenceTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ConferenceTime conferenceTime1 = (ConferenceTime) o;
        return conferenceTime == conferenceTime1.conferenceTime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(conferenceTime);
    }
}
