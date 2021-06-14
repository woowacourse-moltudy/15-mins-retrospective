package wooteco.retrospective.application;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.convert.DataSizeUnit;
import wooteco.retrospective.application.dto.MemberResponseDto;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.application.pair.PairService;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.dao.AttendanceDao;
import wooteco.retrospective.domain.dao.PairDao;
import wooteco.retrospective.domain.dao.TimeDao;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.Pairs;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.retrospective.common.Fixture.*;


class PairServiceTest {
    private static final LocalDate TODAY = LocalDate.of(2021, 6, 14);
    private static final LocalDate YESTERDAY = LocalDate.of(2021, 6, 13);

    private static PairService pairService;
    private static TimeDao timeDao;
    private static PairDao pairDao;
    private static AttendanceDao attendanceDao;

    private static List<Attendance> yesterdayAttendances = List.of(
            new Attendance(1L, YESTERDAY, neozal.getMember(), new Time(TIME_SIX)),
            new Attendance(2L, YESTERDAY, danijani.getMember(), new Time(TIME_SIX)),
            new Attendance(3L, YESTERDAY, whyguy.getMember(), new Time(TIME_SIX))
    );
    private static Pair yesterdayPair = new Pair(1L ,yesterdayAttendances);

    private static Pair pairOne = new Pair(1L, List.of(neozal, whyguy, duck));
    private static Pair pairTwo = new Pair(2L, List.of(danijani, soulg, chu));

    @BeforeAll
    static void beforeAll() {
        timeDao = new TimeDao.Fake();
        pairDao = new PairDao.Fake();
        attendanceDao = new AttendanceDao.Fake();
        pairService = new PairService(timeDao, pairDao, attendanceDao);

        pairDao.insert(
                Pairs.from(List.of(
                        pairOne,
                        pairTwo
                ))
        );

        yesterdayAttendances.forEach(attendanceDao::insert);
    }

    @DisplayName("페어를 반환한다.")
    @Test
    void getPairsByDateAndTime() {
        List<PairResponseDto> pairResponse = pairService.getPairsByDateAndTime(
                TODAY,
                LocalTime.of(18, 0),
                LocalTime.of(19, 0)
        );

        assertThat(pairResponse)
                .usingRecursiveComparison()
                .isEqualTo(List.of(
                        new PairResponseDto(pairOne),
                        new PairResponseDto(pairTwo)
                ));
    }

    @DisplayName("페어를 두번 요청해도 동일한 값이 반환된다.")
    @Test
    void getPairsByDateAndTimeWithTwice() {
        List<PairResponseDto> once = pairService.getPairsByDateAndTime(
                TODAY,
                LocalTime.of(18, 0),
                LocalTime.of(19, 0)
        );

        List<PairResponseDto> twice = pairService.getPairsByDateAndTime(
                TODAY,
                LocalTime.of(18, 0),
                LocalTime.of(20, 0)
        );

        assertThat(once)
                .usingRecursiveComparison()
                .isEqualTo(twice);
    }

    @DisplayName("날짜가 지난 회고의 페어를 요청하면 반환한다.")
    @Test
    void getPairsByDateAndTimeWithBeforeDays() {
        List<PairResponseDto> pairs = pairService.getPairsByDateAndTime(
                YESTERDAY,
                LocalTime.of(18, 0),
                LocalTime.of(19, 0)
        );

        List<String> actual = pairs.stream()
                .flatMap(p -> p.getMembers().stream())
                .map(MemberResponseDto::getName)
                .sorted()
                .collect(toList());

        List<String> expected = yesterdayPair.getAttendances().stream()
                .map(Attendance::getMember)
                .map(Member::getName)
                .sorted()
                .collect(toList());

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("미래 날짜를 요청하면 예외")
    @Test
    void getPairsByDateAndTimeFailWithBadDate() {
        assertThatThrownBy(
                () -> pairService.getPairsByDateAndTime(
                        LocalDate.now().plusDays(1),
                        LocalTime.of(18, 0),
                        LocalTime.of(19, 0)
                )
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("회고 시간 전에 매칭을 요청하면 예외")
    @ParameterizedTest
    @CsvSource({"22, 21", "18, 17", "22, 17"})
    void getPairsByDateAndTimeFailWithBadTime(int requestTime, int currentTime) {
        assertThatThrownBy(
                () -> pairService.getPairsByDateAndTime(
                        LocalDate.now(),
                        LocalTime.of(requestTime, 0),
                        LocalTime.of(currentTime, 0)
                )
        ).isInstanceOf(RuntimeException.class);
    }

    @DisplayName("잘못된 회고 시간을 줄 경우 예외")
    @Test
    void getPairsByDateAndTimeFailWithBadConferenceTime() {
        assertThatThrownBy(
                () -> pairService.getPairsByDateAndTime(
                        LocalDate.now(),
                        LocalTime.of(1, 0),
                        LocalTime.now()
                )
        ).isInstanceOf(RuntimeException.class);
    }
}
