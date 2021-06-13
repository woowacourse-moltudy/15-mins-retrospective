package wooteco.retrospective.domain.pair.matchpolicy;

import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

public class DefaultMatchPolicy implements MatchPolicy {

    private static final int NUMBER_OF_PAIR = 3;
    private static final int MINIMUM_PAIRS_SIZE = 2;
    public static final int FIRST_GROUP_ID = 1;

    @Override
    public List<Pair> apply(List<Member> members) {
        validateNumberOfPairs(members);

        AtomicLong groupId = new AtomicLong(FIRST_GROUP_ID);

        return createPairs(members).stream()
                .map(pair -> new Pair(groupId.getAndIncrement(), pair))
                .collect(toList());
    }

    private void validateNumberOfPairs(List<Member> pairs) {
        if (pairs.size() < MINIMUM_PAIRS_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    private List<List<Member>> createPairs(final List<Member> members) {
        if (members.isEmpty()) {
            return new ArrayList<>();
        }

        int numberOfPair = setNumberOfPair(members);

        List<List<Member>> pairs = createPairs(
                members.subList(Math.min(numberOfPair, members.size()), members.size())
        );

        List<Member> pair = new ArrayList<>(members.subList(0, Math.min(numberOfPair, members.size())));
        pairs.add(pair);

        return pairs;
    }

    private int setNumberOfPair(List<Member> members) {
        int numberOfPair = NUMBER_OF_PAIR;
        if (members.size() % NUMBER_OF_PAIR == 1 || members.size() % 3 == MINIMUM_PAIRS_SIZE) {
            numberOfPair = MINIMUM_PAIRS_SIZE;
        }

        return numberOfPair;
    }

}
