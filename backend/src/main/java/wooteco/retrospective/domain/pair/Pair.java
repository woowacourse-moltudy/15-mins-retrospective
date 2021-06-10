package wooteco.retrospective.domain.pair;

import wooteco.retrospective.domain.pair.member.Member;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Pair {

    private final List<Member> members;

    public Pair(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair pair = (Pair) o;
        return Objects.equals(members, pair.members);
    }

    @Override
    public int hashCode() {
        return Objects.hash(members);
    }

}
