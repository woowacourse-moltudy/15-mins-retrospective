package wooteco.retrospective.domain.pair.matchpolicy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.Pairs;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.retrospective.common.Fixture.neozal;
import static wooteco.retrospective.common.Fixture.provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy;

class DefaultMatchPolicyTest {

    private static final MatchPolicy matchPolicy = new DefaultMatchPolicy();

    private static Stream<Arguments> provideMemberList() {
        return provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy();
    }

    @DisplayName("사용자 리스트가 2보다 작으면 예외")
    @Test
    void apply_inFalseCase() {
        assertThatThrownBy(() -> Pairs.withDefaultMatchPolicy(Collections.singletonList(neozal)).getPairs())
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> Pairs.withDefaultMatchPolicy(Collections.emptyList()).getPairs())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("사용자 리스트를 받으면, 회고 가능한 인원대로 페어로 만들어준다.")
    @ParameterizedTest
    @MethodSource("provideMemberList")
    void apply_makePairAccordingToTheRightNumberOfPeople(List<Member> members, List<Integer> expected) {
        List<Pair> pairs = matchPolicy.apply(members);

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
    @MethodSource("provideMemberList")
    void apply_membersCannotBeDuplicated(List<Member> members) {
        List<Pair> pairs = matchPolicy.apply(members);

        long actual = pairs.stream()
                .distinct()
                .mapToLong(pair -> pair.getMembers().size())
                .sum();

        assertThat(actual).isEqualTo(members.size());
    }

}
