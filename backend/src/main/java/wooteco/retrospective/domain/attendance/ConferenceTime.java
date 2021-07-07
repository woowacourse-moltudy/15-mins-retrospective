package wooteco.retrospective.domain.attendance;

import wooteco.retrospective.application.dto.ConferenceTimeDto;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Entity
public class ConferenceTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalTime conferenceTime;

    @OneToMany(mappedBy = "conferenceTime", cascade = CascadeType.PERSIST)
    private List<Attendance> attendances;

    public ConferenceTime(LocalTime conferenceTime) {
        this(null, conferenceTime);
    }

    public ConferenceTime(Long id, LocalTime conferenceTime) {
        this.id = id;
        this.conferenceTime = conferenceTime;
    }

    protected ConferenceTime() {

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
