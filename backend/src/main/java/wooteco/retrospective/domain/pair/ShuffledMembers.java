package wooteco.retrospective.domain.pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

public class ShuffledMembers implements Shuffled<Member> {
    private static final UnaryOperator<List<Member>> DEFAULT_SHUFFLE_POLICY = members -> {
        List<Member> copiedMembers = new ArrayList<>(members);
        Collections.shuffle(copiedMembers);

        return copiedMembers;
    };

    private final List<Member> members;

    public ShuffledMembers(List<Member> unShuffledMember, UnaryOperator<List<Member>> shufflePolicy) {
        this.members = shufflePolicy.apply(unShuffledMember);
    }

    public ShuffledMembers(List<Member> unShuffledMembers) {
        this(unShuffledMembers, DEFAULT_SHUFFLE_POLICY);
    }

    @Override
    public List<Member> value() {
        return new ArrayList<>(members);
    }

}
