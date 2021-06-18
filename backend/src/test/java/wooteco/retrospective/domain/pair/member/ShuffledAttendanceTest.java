package wooteco.retrospective.domain.pair.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.member.Member;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.retrospective.common.Fixture.*;

class ShuffledAttendanceTest {

    @DisplayName("셔플된 맴버를 반환한다.")
    @ParameterizedTest
    @MethodSource("provideShufflePolicies")
    void value_returnShuffledMembers(UnaryOperator<List<Attendance>> shufflePolicy, List<Attendance> expected) {
        Shuffled<Attendance> shuffledMembers = new ShuffledAttendances(
                List.of(neozal, whyguy, danijani),
                shufflePolicy
        );

        assertThat(shuffledMembers.value()).containsExactlyElementsOf(expected);
    }

    private static Stream<Arguments> provideShufflePolicies() {
        return Stream.of(
                Arguments.of(
                        (UnaryOperator<List<Attendance>>) members -> List.of(neozal, whyguy, danijani),
                        List.of(neozal, whyguy, danijani)
                ),
                Arguments.of(
                        (UnaryOperator<List<Attendance>>) members -> List.of(whyguy, neozal, danijani),
                        List.of(whyguy, neozal, danijani)
                ),
                Arguments.of(
                        (UnaryOperator<List<Attendance>>) members -> List.of(danijani, whyguy, neozal),
                        List.of(danijani, whyguy, neozal)
                )
        );
    }

}
