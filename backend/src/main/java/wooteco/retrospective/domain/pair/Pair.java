package wooteco.retrospective.domain.pair;

import wooteco.retrospective.domain.member.Member;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Pair {

    private final Long groupId;
    private final List<Member> members;

    public Pair(Long groupId, List<Member> members) {
        this.groupId = groupId;
        this.members = members;
    }

    public Long getGroupId() {
        return groupId;
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
