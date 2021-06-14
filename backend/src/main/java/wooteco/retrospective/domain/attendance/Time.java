package wooteco.retrospective.domain.attendance;

import java.time.LocalTime;
import java.util.Objects;

public class Time {

    private final Long id;
    private final LocalTime time;

    public Time(LocalTime time) {
        this(null, time);
    }

    public Time(Long id, LocalTime time) {
        this.id = id;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public LocalTime getTime() {
        return time;
    }

    public boolean isBefore(Time time) {
        return this.time.isBefore(time.getTime());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Time time1 = (Time) o;
        return time == time1.time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }
}
