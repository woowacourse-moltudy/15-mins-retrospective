package wooteco.retrospective.domain.attendance;

import wooteco.retrospective.domain.member.Member;

import java.time.LocalDate;
import java.util.Objects;

public class Attendance {

    private final Long id;
    private final LocalDate date;
    private final Member member;
    private final Time time;

    public Attendance(Member member, Time time) {
        this(LocalDate.now(), member, time);
    }

    public Attendance(LocalDate localDate, Member member, Time time) {
        this(null, localDate, member, time);
    }

    public Attendance(Long id, LocalDate date, Member member, Time time) {
        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
    }

    public boolean isAttendAt(Time time) {
        return this.time.equals(time);
    }

    public boolean isAttendAt(LocalDate date) {
        return this.date.equals(date);
    }

    public long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public Member getMember() {
        return member;
    }

    public Time getTime() {
        return time;
    }

    public long getMemberId() {
        return member.getId();
    }

    public long getTimeId() {
        return time.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Attendance that = (Attendance) o;
        return date.equals(that.date) && member.getName().equals(that.member.getName()) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, member, time);
    }
}
