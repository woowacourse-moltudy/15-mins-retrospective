package wooteco.retrospective.domain.attendance;

import java.util.Objects;

public class Time {

    private final Long id;
    private final int time;

    public Time(int time) {
        this(null, time);
    }

    public Time(Long id, int time) {
        validate(time);
        this.id = id;
        this.time = time;
    }

    private void validate(int time) {
        if (time < 0 || time > 24) {
            throw new IllegalArgumentException("옳지 않은 시간입니다.");
        }
    }

    public long getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Time time1 = (Time)o;
        return time == time1.time;
    }

    @Override
    public int hashCode() {
        return Objects.hash(time);
    }
}
