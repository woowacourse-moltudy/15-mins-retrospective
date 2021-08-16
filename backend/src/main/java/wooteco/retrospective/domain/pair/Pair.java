package wooteco.retrospective.domain.pair;

import wooteco.retrospective.domain.attendance.Attendance;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Entity
public class Pair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long groupId;

    @OneToMany(
            mappedBy = "pair",
            fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST
    )
    private List<Attendance> attendances;

    protected Pair() {
    }

    public Pair(Long groupId, List<Attendance> attendances) {
        this.groupId = groupId;
        this.attendances = attendances;
        this.attendances.forEach(attendance -> attendance.appendTo(this));
    }

    public Long getId() {
        return id;
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
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return Objects.equals(pair.getId(), getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
