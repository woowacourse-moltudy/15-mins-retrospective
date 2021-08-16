package wooteco.retrospective.domain.conference_time;

import wooteco.retrospective.application.dto.ConferenceTimeDto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;
import java.util.Objects;

@Entity
public class ConferenceTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalTime conferenceTime;

    protected ConferenceTime() {
    }

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

    public boolean isBefore(LocalTime conferenceTime) {
        return this.conferenceTime.isBefore(conferenceTime);
    }

    public Long getId() {
        return id;
    }

    public LocalTime getConferenceTime() {
        return conferenceTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ConferenceTime)) return false;
        ConferenceTime that = (ConferenceTime) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
