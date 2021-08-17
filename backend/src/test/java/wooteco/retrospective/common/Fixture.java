package wooteco.retrospective.common;

import org.junit.jupiter.params.provider.Arguments;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.member.Member;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Stream;

public class Fixture {

    public static final LocalDate TODAY = LocalDate.of(2021, 6, 14);
    public static final LocalDate YESTERDAY = LocalDate.of(2021, 6, 13);
    public static final LocalTime TIME_SIX = LocalTime.of(18, 0);

    public static final Attendance neozal = new Attendance(1L, TODAY, new Member(1L, "손너잘"), new ConferenceTime(TIME_SIX));
    public static final Attendance whyguy = new Attendance(2L, TODAY, new Member(2L, "웨지"), new ConferenceTime(TIME_SIX));
    public static final Attendance danijani = new Attendance(3L, TODAY, new Member(3L, "다니"), new ConferenceTime(TIME_SIX));
    public static final Attendance soulg = new Attendance(4L, TODAY, new Member(4L, "솔지"), new ConferenceTime(TIME_SIX));
    public static final Attendance chu = new Attendance(5L, TODAY, new Member(5L, "피카"), new ConferenceTime(TIME_SIX));
    public static final Attendance spring = new Attendance(6L, TODAY, new Member(6L, "나봄"), new ConferenceTime(TIME_SIX));
    public static final Attendance duck = new Attendance(7L, TODAY, new Member(7L, "조연우"), new ConferenceTime(TIME_SIX));

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
