package wooteco.retrospective.domain.pair.matchpolicy;

import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.member.Member;

import java.util.List;

public interface MatchPolicy {

    List<Pair> apply(final List<Member> members);

}
