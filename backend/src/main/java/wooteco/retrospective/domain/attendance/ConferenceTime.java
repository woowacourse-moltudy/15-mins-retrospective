package wooteco.retrospective.domain.attendance;

import java.time.LocalTime;
import java.util.Objects;

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
