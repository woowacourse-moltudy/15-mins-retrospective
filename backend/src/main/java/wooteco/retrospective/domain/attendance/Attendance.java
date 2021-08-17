package wooteco.retrospective.domain.attendance;

import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "CONFERENCE_TIME_ID")
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

    public Long getMemberId() {
        return member.getId();
    }

    public Long getConferenceTimeId() {
        return conferenceTime.getId();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Attendance)) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
