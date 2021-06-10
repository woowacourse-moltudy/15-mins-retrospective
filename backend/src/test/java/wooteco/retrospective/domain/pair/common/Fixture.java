package wooteco.retrospective.domain.pair.common;

import org.junit.jupiter.params.provider.Arguments;
import wooteco.retrospective.domain.pair.member.Member;

import java.util.List;
import java.util.stream.Stream;

public class Fixture {

    public static final Member neozal = new Member("손너잘");
    public static final Member whyguy = new Member("웨지");
    public static final Member danijani = new Member("다니");
    public static final Member soulg = new Member("솔지");
    public static final Member chu = new Member("피카");
    public static final Member spring = new Member("나봄");
    public static final Member duck = new Member("조연우");

    public static Stream<Arguments> provideMemberListAndMatchedPairSizesOnDefaultMatchPolicy() {
        return Stream.of(
                Arguments.of(List.of(neozal, whyguy), List.of(2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg), List.of(2, 2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu), List.of(3, 2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu, spring), List.of(3, 3)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu, spring, duck), List.of(3, 2, 2))
        );
    }

}
