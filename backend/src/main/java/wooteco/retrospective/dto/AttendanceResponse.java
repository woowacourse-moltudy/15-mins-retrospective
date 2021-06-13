package wooteco.retrospective.dto;

import java.time.LocalDate;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;

public class AttendanceResponse {

    private final LocalDate date;
    private final Member member;
    private final Time time;

    public AttendanceResponse(LocalDate date, Member member, Time time) {
        this.date = date;
        this.member = member;
        this.time = time;
    }

    public static AttendanceResponse of(Attendance attendance) {
        return new AttendanceResponse(attendance.getDate(), attendance.getMember(), attendance.getTime());
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
}
