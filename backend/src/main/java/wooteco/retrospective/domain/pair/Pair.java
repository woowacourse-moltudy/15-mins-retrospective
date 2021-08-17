package wooteco.retrospective.domain.pair;

import wooteco.retrospective.domain.attendance.Attendance;

import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Pair {

    private Long groupId;
    private List<Attendance> attendances;

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

    public List<GroupedAttendance> toGroupedPairs() {
        return attendances.stream()
                .map(attendance ->  new GroupedAttendance(groupId, attendance))
                .collect(toList());
    }
}
