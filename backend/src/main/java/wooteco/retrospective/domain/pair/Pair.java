package wooteco.retrospective.domain.pair;

import wooteco.retrospective.domain.pair.member.Member;

import java.util.Collections;
import java.util.List;

public class Pair {

    private final List<Member> members;

    public Pair(List<Member> members) {
        this.members = members;
    }

    public List<Member> getMembers() {
        return Collections.unmodifiableList(members);
    }

}
