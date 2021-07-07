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
    @Column(nullable = false)
    private Long groupId;
    @OneToMany(mappedBy = "pair", cascade = CascadeType.PERSIST)
    private List<Attendance> attendances;

    public Pair(Long groupId, List<Attendance> attendances) {
        this.groupId = groupId;
        this.attendances = attendances;
    }

    protected Pair() {

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
