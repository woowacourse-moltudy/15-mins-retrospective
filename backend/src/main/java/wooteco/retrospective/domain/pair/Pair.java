package wooteco.retrospective.domain.pair;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import wooteco.retrospective.domain.attendance.Attendance;

@Entity
public class Pair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId;

    @OneToMany(
        mappedBy = "pair",
        cascade = CascadeType.PERSIST,
        fetch = FetchType.EAGER
    )
    private List<Attendance> attendances;

    protected Pair() {
    }

    public Pair(Long groupId, List<Attendance> attendances) {
        this.groupId = groupId;
        this.attendances = attendances;
        this.attendances.forEach(attendance -> attendance.appendTo(this));
    }

    public Long getGroupId() {
        return groupId;
    }

    public List<Attendance> getAttendances() {
        return Collections.unmodifiableList(attendances);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(groupId, pair.groupId) && Objects.equals(attendances, pair.attendances);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, attendances);
    }
}
