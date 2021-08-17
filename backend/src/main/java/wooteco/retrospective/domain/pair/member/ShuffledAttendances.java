package wooteco.retrospective.domain.pair.member;

import wooteco.retrospective.domain.attendance.Attendance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

public class ShuffledAttendances implements Shuffled<Attendance> {

    private static final UnaryOperator<List<Attendance>> DEFAULT_SHUFFLE_POLICY = attendances -> {
        List<Attendance> copiedMembers = new ArrayList<>(attendances);
        Collections.shuffle(copiedMembers);

        return copiedMembers;
    };

    private final List<Attendance> attendances;

    public ShuffledAttendances(List<Attendance> unShuffledAttendances, UnaryOperator<List<Attendance>> shufflePolicy) {
        this.attendances = shufflePolicy.apply(unShuffledAttendances);
    }

    public ShuffledAttendances(List<Attendance> unShuffledAttendances) {
        this(unShuffledAttendances, DEFAULT_SHUFFLE_POLICY);
    }

    @Override
    public List<Attendance> value() {
        return new ArrayList<>(attendances);
    }

}
