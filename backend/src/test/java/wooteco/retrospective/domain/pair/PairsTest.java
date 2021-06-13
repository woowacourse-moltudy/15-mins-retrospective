package wooteco.retrospective.domain.pair;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.retrospective.common.Fixture;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.matchpolicy.MatchPolicy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class PairsTest {

    private static Stream<Arguments> provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy() {
        return Fixture.provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy();
    }

    @DisplayName("기본 matchPolicy 를 이용하여 페어 매칭을 진행한다.")
    @ParameterizedTest
    @MethodSource("provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy")
    void getPairs_pairMatchWithDefaultPolicy(List<Member> members, List<Integer> expected) {
        List<Pair> pairs = new Pairs(members).getPairs();

        List<List<Member>> pairMembers = pairs.stream()
                .map(Pair::getMembers)
                .collect(toList());

        List<Integer> actual = pairMembers.stream()
                .map(List::size)
                .collect(toList());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("페어 생성 시 멤버 중복은 허용하지 않는다.")
    @ParameterizedTest
    @MethodSource("provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy")
    void getPairs_cannotAllowDuplicatedMemberForAllPairs(List<Member> members) {
        List<Pair> pairs = new Pairs(members).getPairs();

        long actual = pairs.stream()
                .distinct()
                .mapToLong(pair -> pair.getMembers().size())
                .sum();

        assertThat(actual).isEqualTo(members.size());
    }

    @DisplayName("기본 matchPolicy 를 이용하여 페어 매칭을 진행한다.")
    @ParameterizedTest
    @MethodSource("provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy")
    void getPairs_inTrueCase(List<Member> members, List<Integer> expected) {
        List<Pair> pairs = new Pairs(members).getPairs();

        List<List<Member>> pairMembers = pairs.stream()
                .map(Pair::getMembers)
                .collect(toList());

        List<Integer> actual = pairMembers.stream()
                .map(List::size)
                .collect(toList());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("customPolicy 를 이용하여 페어 매칭을 진행한다.")
    @ParameterizedTest
    @MethodSource("provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy")
    void getPairs_withCustomPolicy(List<Member> members) {
        final MatchPolicy matchPolicy = memberList -> Collections.singletonList(
                new Pair(1L, memberList)
        );

        List<Pair> actual = new Pairs(members, matchPolicy).getPairs();

        assertThat(actual).isEqualTo(Collections.singletonList(new Pair(1L, members)));
    }

}
