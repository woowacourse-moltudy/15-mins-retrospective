package wooteco.retrospective.domain.pair.matchpolicy;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.pair.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

public class DefaultMatchPolicy implements MatchPolicy {

    private static final int NUMBER_OF_ATTENDANCE_FOR_PAIR = 3;
    private static final int MINIMUM_PAIRS_SIZE = 2;
    public static final int FIRST_GROUP_ID = 1;

    @Override
    public List<Pair> apply(List<Attendance> attendances) {
        validateNumberOfAttendances(attendances);

        AtomicLong groupId = new AtomicLong(FIRST_GROUP_ID);

        return createCandidatesOfPairs(attendances).stream()
                .map(pair -> new Pair(groupId.getAndIncrement(), pair))
                .collect(toList());
    }

    private void validateNumberOfAttendances(List<Attendance> pairs) {
        if(pairs.size() < MINIMUM_PAIRS_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private List<List<Attendance>> createCandidatesOfPairs(final List<Attendance> attendances) {
        if (attendances.isEmpty()) {
            return new ArrayList<>();
        }

        int numberOfPair = setNumberOfAttendanceForPair(attendances);

        List<List<Attendance>> pairs = createCandidatesOfPairs(
                attendances.subList(Math.min(numberOfPair, attendances.size()), attendances.size())
        );

        List<Attendance> pair = new ArrayList<>(
                attendances.subList(0, Math.min(numberOfPair, attendances.size()))
        );
        pairs.add(pair);

        return pairs;
    }

    private int setNumberOfAttendanceForPair(List<Attendance> members) {
        int numberOfPair = NUMBER_OF_ATTENDANCE_FOR_PAIR;
        if (
                members.size() % NUMBER_OF_ATTENDANCE_FOR_PAIR == 1 ||
                members.size() % 3 == MINIMUM_PAIRS_SIZE
        ) {
            numberOfPair = MINIMUM_PAIRS_SIZE;
        }

        return numberOfPair;
    }
}
