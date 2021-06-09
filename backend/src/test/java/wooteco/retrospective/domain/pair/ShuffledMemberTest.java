package wooteco.retrospective.domain.pair;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class ShuffledMemberTest {

    private static final Member neozal = new Member("손너잘");
    private static final Member whyGuy = new Member("웨지");
    private static final Member daniJani = new Member("다니");

    @DisplayName("셔플된 맴버를 반환한다.")
    @ParameterizedTest
    @MethodSource("provideShufflePolicies")
    void value_returnShuffledMembers(UnaryOperator<List<Member>> shufflePolicy, List<Member> expected) {
        Shuffled<Member> shuffledMembers = new ShuffledMembers(
                List.of(neozal, whyGuy, daniJani),
                shufflePolicy
        );

        assertThat(shuffledMembers.value()).containsExactlyElementsOf(expected);
    }

    private static Stream<Arguments> provideShufflePolicies() {
        return Stream.of(
                Arguments.of(
                        (UnaryOperator<List<Member>>) members -> List.of(neozal, whyGuy, daniJani),
                        List.of(neozal, whyGuy, daniJani)
                ),
                Arguments.of(
                        (UnaryOperator<List<Member>>) members -> List.of(whyGuy, neozal, daniJani),
                        List.of(whyGuy, neozal, daniJani)
                ),
                Arguments.of(
                        (UnaryOperator<List<Member>>) members -> List.of(daniJani, whyGuy, neozal),
                        List.of(daniJani, whyGuy, neozal)
                )
        );
    }

}