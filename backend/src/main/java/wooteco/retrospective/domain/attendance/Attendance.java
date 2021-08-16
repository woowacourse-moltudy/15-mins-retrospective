package wooteco.retrospective.domain.attendance;

import java.time.LocalDate;
import java.util.Objects;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.member.Member;

public class Attendance {

    private Long id;
    private LocalDate date;
    private Member member;
    private ConferenceTime conferenceTime;

    protected Attendance() {
    }

    public Attendance(Member member, ConferenceTime conferenceTime) {
        this(LocalDate.now(), member, conferenceTime);
    }

    public Attendance(LocalDate localDate, Member member, ConferenceTime conferenceTime) {
        this(null, localDate, member, conferenceTime);
    }

    public Attendance(Long id, LocalDate date, Member member, ConferenceTime conferenceTime) {
        this.id = id;
        this.date = date;
        this.member = member;
        this.conferenceTime = conferenceTime;
    }

    public boolean isAttendAt(ConferenceTime conferenceTime) {
        return this.conferenceTime.equals(conferenceTime);
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

    public ConferenceTime getConferenceTime() {
        return conferenceTime;
    }

    public long getMemberId() {
        return member.getId();
    }

    public long getConferenceTimeId() {
        return conferenceTime.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Attendance that = (Attendance) o;
        return date.equals(that.date) && member.getName().equals(that.member.getName()) && conferenceTime.equals(that.conferenceTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, member, conferenceTime);
    }
}
