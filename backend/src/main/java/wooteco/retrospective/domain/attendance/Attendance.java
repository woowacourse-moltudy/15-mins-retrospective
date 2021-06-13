package wooteco.retrospective.domain.attendance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import wooteco.retrospective.domain.member.Member;

public class Attendance {

    private final Long id;
    private final String date;
    private final Member member;
    private final Time time;

    public Attendance(LocalDateTime localDateTime, Member member, Time time) {
        this(null, localDateTime, member, time);
    }

    public Attendance(String date, Member member, Time time) {
        this(null, date, member, time);
    }

    public Attendance(Long id, LocalDateTime localDateTime, Member member, Time time) {
        this(id, localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), member, time);
    }

    public Attendance(Long id, String date, Member member, Time time) {
        this.id = id;
        this.date = date;
        this.member = member;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
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
        Attendance that = (Attendance)o;
        return date.equals(that.date) && member.getName().equals(that.member.getName()) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, member, time);
    }
}
