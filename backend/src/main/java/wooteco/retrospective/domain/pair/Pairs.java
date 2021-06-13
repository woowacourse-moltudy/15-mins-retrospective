package wooteco.retrospective.domain.pair;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.matchpolicy.DefaultMatchPolicy;
import wooteco.retrospective.domain.pair.matchpolicy.MatchPolicy;
import wooteco.retrospective.domain.pair.member.Shuffled;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Pairs {
    private static final MatchPolicy DEFAULT_MATCH_POLICY = new DefaultMatchPolicy();

    private List<Pair> pairs;

    private Pairs(final List<Attendance> attendances, MatchPolicy matchPolicy) {
        this.pairs = matchPolicy.apply(attendances);
    }

    private Pairs(final List<Pair> pairs) {
        this.pairs = pairs;
    }

    public static Pairs withDefaultMatchPolicy(final List<Attendance> attendances) {
        return new Pairs(attendances, DEFAULT_MATCH_POLICY);
    }

    public static Pairs withDefaultMatchPolicy(final Shuffled<Attendance> attendances) {
        return new Pairs(attendances.value(), DEFAULT_MATCH_POLICY);
    }

    public static Pairs from(final List<Attendance> attendances, MatchPolicy matchPolicy) {
        return new Pairs(attendances, matchPolicy);
    }

    public static Pairs from(final Shuffled<Attendance> attendances, MatchPolicy matchPolicy) {
        return new Pairs(attendances.value(),matchPolicy);
    }

    public static Pairs from(List<Pair> pairs) {
        return new Pairs(pairs);
    }

    public synchronized List<Pair> getPairs() {
        return Collections.unmodifiableList(this.pairs);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pairs pairs1 = (Pairs) o;
        return pairs.equals(pairs1.pairs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pairs);
    }
}
