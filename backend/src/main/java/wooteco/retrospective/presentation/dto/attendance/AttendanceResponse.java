package wooteco.retrospective.presentation.dto.attendance;

import wooteco.retrospective.application.dto.AttendanceDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.member.Member;

import java.time.LocalDate;

public class AttendanceResponse {

    private final LocalDate date;
    private final Member member;
    private final ConferenceTime conferenceTime;

    public AttendanceResponse(LocalDate date, Member member, ConferenceTime conferenceTime) {
        this.date = date;
        this.member = member;
        this.conferenceTime = conferenceTime;
    }

    public static AttendanceResponse of(AttendanceDto attendanceDto) {
        return new AttendanceResponse(attendanceDto.getDate(), attendanceDto.getMember(), attendanceDto.getConferenceTime());
    }

    public LocalDate getDate() {
        return date;
    }

    public Member getMember() {
        return member;
    }

    public ConferenceTime getConferenceTime() {
        return conferenceTime;
    }
}
