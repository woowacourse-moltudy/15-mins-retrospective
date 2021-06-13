package wooteco.retrospective.domain.pair;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.retrospective.common.Fixture;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.matchpolicy.MatchPolicy;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class PairsTest {

    @DisplayName("기본 matchPolicy 를 이용하여 페어 매칭을 진행한다.")
    @ParameterizedTest
    @MethodSource("provideAttendanceListAndMatchedPairSizesOnDefaultMatchPolicy")
    void getPairs_pairMatchWithDefaultPolicy(List<Attendance> members, List<Integer> expected) {
        List<Pair> pairs = Pairs.withDefaultMatchPolicy(members).getPairs();

        List<List<Attendance>> pairAttendances = pairs.stream()
                .map(Pair::getAttendances)
                .collect(toList());

        List<Integer> actual = pairAttendances.stream()
                .map(List::size)
                .collect(toList());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("페어 생성 시 멤버 중복은 허용하지 않는다.")
    @ParameterizedTest
    @MethodSource("provideAttendanceListAndMatchedPairSizesOnDefaultMatchPolicy")
    void getPairs_cannotAllowDuplicatedMemberForAllPairs(List<Attendance> attendances) {
        List<Pair> pairs = Pairs.withDefaultMatchPolicy(attendances).getPairs();

        long actual = pairs.stream()
                .distinct()
                .mapToLong(pair -> pair.getAttendances().size())
                .sum();

        assertThat(actual).isEqualTo(attendances.size());
    }

    private static Stream<Arguments> provideAttendanceListAndMatchedPairSizesOnDefaultMatchPolicy() {
        return Fixture.provideAttendanceListAndMatchedPairSizesOnDefaultMatchPolicy();
    }

    @DisplayName("기본 matchPolicy 를 이용하여 페어 매칭을 진행한다.")
    @ParameterizedTest
    @MethodSource("provideAttendanceListAndMatchedPairSizesOnDefaultMatchPolicy")
    void getPairs_inTrueCase(List<Attendance> attendances, List<Integer> expected) {
        List<Pair> pairs = Pairs.withDefaultMatchPolicy(attendances).getPairs();

        List<List<Attendance>> pairAttendances = pairs.stream()
                .map(Pair::getAttendances)
                .collect(toList());

        List<Integer> actual = pairAttendances.stream()
                .map(List::size)
                .collect(toList());

        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @DisplayName("customPolicy 를 이용하여 페어 매칭을 진행한다.")
    @ParameterizedTest
    @MethodSource("provideAttendanceListAndMatchedPairSizesOnDefaultMatchPolicy")
    void getPairs_withCustomPolicy(List<Attendance> attendances) {
        final MatchPolicy matchPolicy = memberList -> Collections.singletonList(
                new Pair(1L, memberList)
        );

        List<Pair> actual = Pairs.from(attendances, matchPolicy).getPairs();

        assertThat(actual).isEqualTo(Collections.singletonList(new Pair(1L, attendances)));
    }

}
