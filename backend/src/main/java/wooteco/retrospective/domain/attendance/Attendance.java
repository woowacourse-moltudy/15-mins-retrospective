package wooteco.retrospective.domain.attendance;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

import wooteco.retrospective.domain.member.Member;

public class Attendance {

    private long id;
    private String date;
    private final Member member;
    private final Time time;

    public Attendance(long id, LocalDateTime timestamp, Member member, Time time) {
        this(timestamp, member, time);
        this.id = id;
    }

    public Attendance(Long id, String date, Member member, Time time) {
        this(date, member, time);
        this.id = id;
    }

    public Attendance(LocalDateTime timestamp, Member member, Time time) {
        this.date = timestamp.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.member = member;
        this.time = time;
    }

    public Attendance(String date, Member member, Time time) {
        this(member, time);
        this.date = date;
    }

    public Attendance(Member member, Time time) {
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
        return date.equals(that.date) && member.equals(that.member) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, member, time);
    }
}
