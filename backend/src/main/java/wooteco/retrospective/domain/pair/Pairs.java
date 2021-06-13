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
    private final List<Member> members;
    private final MatchPolicy matchPolicy;
    private List<Pair> pairs;

    public Pairs(final List<Member> pairs) {
        this(pairs, DEFAULT_MATCH_POLICY);
    }

    public Pairs(final Shuffled<Member> pairs) {
        this(pairs.value(), DEFAULT_MATCH_POLICY);
    }

    public Pairs(final Shuffled<Member> pairs, MatchPolicy matchPolicy) {
        this(pairs.value(), matchPolicy);
    }

    private Pairs(final List<Pair> pairs, List<Member> members, MatchPolicy matchPolicy) {
        this(members, matchPolicy);
        this.pairs = pairs;
    }

    public Pairs(final List<Member> pairs, MatchPolicy matchPolicy) {
        this.members = pairs;
        this.matchPolicy = matchPolicy;
    }

    public static Pairs from(List<Pair> pairs) {
        return new Pairs(pairs, Collections.emptyList(), null);
    }

    public synchronized List<Pair> getPairs() {
        if (pairs == null) {
            this.pairs = matchPolicy.apply(this.members);
        }

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
