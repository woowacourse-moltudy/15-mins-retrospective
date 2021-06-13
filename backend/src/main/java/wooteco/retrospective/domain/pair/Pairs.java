package wooteco.retrospective.domain.pair;

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

    private Pairs(final List<Member> members, MatchPolicy matchPolicy) {
        this.pairs = matchPolicy.apply(members);
    }

    private Pairs(final List<Pair> pairs) {
        this.pairs = pairs;
    }

    public static Pairs withDefaultMatchPolicy(final List<Member> members) {
        return new Pairs(members, DEFAULT_MATCH_POLICY);
    }

    public static Pairs withDefaultMatchPolicy(final Shuffled<Member> members) {
        return new Pairs(members.value(), DEFAULT_MATCH_POLICY);
    }

    public static Pairs from(final List<Member> members, MatchPolicy matchPolicy) {
        return new Pairs(members, matchPolicy);
    }

    public static Pairs from(final Shuffled<Member> members, MatchPolicy matchPolicy) {
        return new Pairs(members.value(),matchPolicy);
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
