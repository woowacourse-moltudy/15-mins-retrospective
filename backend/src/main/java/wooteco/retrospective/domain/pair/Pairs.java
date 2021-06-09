package wooteco.retrospective.domain.pair;

import wooteco.retrospective.domain.pair.member.Member;
import wooteco.retrospective.domain.pair.member.Shuffled;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class Pairs {
    private static final int NUMBER_OF_PAIR = 3;
    private static final int MINIMUM_PAIRS_SIZE = 2;

    private List<Pair> pairs;
    private final List<Member> members;

    public Pairs(final List<Member> pairs) {
        validateNumberOfPairs(pairs);
        this.members = pairs;
    }

    public Pairs(final Shuffled<Member> pairs) {
        this(pairs.value());
    }

    private void validateNumberOfPairs(List<Member> pairs) {
        if(pairs.size() < MINIMUM_PAIRS_SIZE) {
            throw new IllegalArgumentException();
        }
    }

    public synchronized List<Pair> getPairs() {
        if (pairs == null) {
            this.pairs = createPairs(members).stream()
                    .map(Pair::new)
                    .collect(toList());
        }

        return Collections.unmodifiableList(this.pairs);
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
