package wooteco.retrospective.domain.pair.matchpolicy;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;

import java.util.List;

public interface MatchPolicy {

    List<Pair> apply(final List<Attendance> attendances);

}
