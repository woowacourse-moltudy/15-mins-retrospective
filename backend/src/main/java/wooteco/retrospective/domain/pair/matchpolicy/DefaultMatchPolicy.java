package wooteco.retrospective.domain.pair.matchpolicy;

import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.member.Member;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DefaultMatchPolicy implements MatchPolicy {

    private static final int NUMBER_OF_PAIR = 3;
    private static final int MINIMUM_PAIRS_SIZE = 2;

    @Override
    public List<Pair> apply(List<Member> members) {
        validateNumberOfPairs(members);

        return createPairs(members).stream()
                .map(Pair::new)
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
