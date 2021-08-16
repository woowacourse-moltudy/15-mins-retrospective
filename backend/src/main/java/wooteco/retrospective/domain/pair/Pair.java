package wooteco.retrospective.domain.pair;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import wooteco.retrospective.domain.attendance.Attendance;

public class Pair {

    private final Long groupId;
    private final List<Attendance> attendances;

    public Pair(Long groupId, List<Attendance> attendances) {
        this.groupId = groupId;
        this.attendances = attendances;
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
