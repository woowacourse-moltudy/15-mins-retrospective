package wooteco.retrospective.domain.pair;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PairsTest {
    private static final Member neozal = new Member("손너잘");
    private static final Member whyguy = new Member("웨지");
    private static final Member danijani = new Member("다니");
    private static final Member soulg = new Member("솔지");
    private static final Member chu = new Member("피카");
    private static final Member spring = new Member("나봄");
    private static final Member duck = new Member("조연우");

    @DisplayName("사용자 리스트가 2보다 작으면 예외")
    @Test
    void getPair_inFalseCase() {
        assertThatThrownBy(() -> new Pairs(Collections.singletonList(neozal)).getPairs())
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new Pairs(Collections.emptyList()).getPairs())
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("사용자 리스트를 받으면, 페어로 만들어준다.")
    @ParameterizedTest
    @MethodSource("provideMemberListForGetPairsTest")
    void getPairs(List<Member> members, List<Integer> expected) {
        List<Pair> pairs = new Pairs(members).getPairs();

        List<List<Member>> pairMembers = pairs.stream()
                .map(Pair::getMembers)
                .collect(toList());

        long countOfAllPairMembers = pairMembers.stream()
                .flatMap(Collection::stream)
                .distinct()
                .count();

        List<Integer> countOfEachPairMembers = pairMembers.stream()
                .map(List::size)
                .collect(toList());

        assertThat(countOfEachPairMembers).usingRecursiveComparison().isEqualTo(expected);
        assertThat(countOfAllPairMembers).isEqualTo(members.size());
    }

    private static Stream<Arguments> provideMemberListForGetPairsTest() {
        return Stream.of(
                Arguments.of(List.of(neozal, whyguy), List.of(2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg), List.of(2, 2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu), List.of(3, 2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu, spring), List.of(3, 3)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu, spring, duck), List.of(3, 2, 2))
        );
    }
    
}