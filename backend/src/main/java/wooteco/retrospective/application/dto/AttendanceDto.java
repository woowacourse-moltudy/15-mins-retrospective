package wooteco.retrospective.application.dto;

import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.member.Member;

import java.time.LocalDate;

public class AttendanceDto {

    private final Long id;
    private final LocalDate date;
    private final Member member;
    private final ConferenceTime conferenceTime;

    public AttendanceDto(Long id, LocalDate date, Member member, ConferenceTime conferenceTime) {
        this.id = id;
        this.date = date;
        this.member = member;
        this.conferenceTime = conferenceTime;
    }

    public Long getId() {
        return id;
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
