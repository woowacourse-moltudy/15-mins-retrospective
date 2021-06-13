package wooteco.retrospective.common;

import org.junit.jupiter.params.provider.Arguments;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

public class Fixture {

    public static final Attendance neozal = new Attendance(new Member("손너잘"), new Time(LocalTime.now()));
    public static final Attendance whyguy = new Attendance(new Member("웨지"), new Time(LocalTime.now()));
    public static final Attendance danijani = new Attendance(new Member("다니"), new Time(LocalTime.now()));
    public static final Attendance soulg = new Attendance(new Member("솔지"), new Time(LocalTime.now()));
    public static final Attendance chu = new Attendance(new Member("피카"), new Time(LocalTime.now()));
    public static final Attendance spring = new Attendance(new Member("나봄"), new Time(LocalTime.now()));
    public static final Attendance duck = new Attendance(new Member("조연우"), new Time(LocalTime.now()));

    public static Stream<Arguments> provideAttendanceListAndMatchedPairSizesOnDefaultMatchPolicy() {
        return Stream.of(
                Arguments.of(List.of(neozal, whyguy), List.of(2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg), List.of(2, 2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu), List.of(3, 2)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu, spring), List.of(3, 3)),
                Arguments.of(List.of(neozal, whyguy, danijani, soulg, chu, spring, duck), List.of(3, 2, 2))
        );
    }

}
