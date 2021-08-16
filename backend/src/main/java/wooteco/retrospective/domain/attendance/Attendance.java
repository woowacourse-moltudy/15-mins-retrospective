package wooteco.retrospective.domain.attendance;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "CONFERENCE_TIME_ID")
    private ConferenceTime conferenceTime;

    @ManyToOne
    @JoinColumn(name = "PAIR_ID")
    private Pair pair;

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

    public void appendTo(Pair pair) {
        this.pair = pair;
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
