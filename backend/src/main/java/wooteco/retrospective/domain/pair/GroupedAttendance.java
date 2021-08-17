package wooteco.retrospective.domain.pair;

import wooteco.retrospective.domain.attendance.Attendance;

import javax.persistence.*;

@Entity
@Table(name = "Pair")
public class GroupedAttendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId;

    @OneToOne(fetch = FetchType.EAGER)
    private Attendance attendance;

    protected GroupedAttendance() {
    }

    public GroupedAttendance(Long groupId, Attendance attendance) {
        this.groupId = groupId;
        this.attendance = attendance;
    }

    public Long getId() {
        return id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public Attendance getAttendance() {
        return attendance;
    }
}

