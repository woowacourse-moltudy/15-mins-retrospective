package wooteco.retrospective.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import wooteco.retrospective.application.dto.MemberResponseDto;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.application.pair.PairService;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.repository.AttendanceRepository;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.conference_time.repository.ConferenceTimeRepository;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.repository.PairRepository;
import wooteco.retrospective.exception.InvalidDateException;
import wooteco.retrospective.exception.InvalidTimeException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.retrospective.common.Fixture.*;
import static wooteco.retrospective.common.Fixture.chu;

@DataJpaTest
class PairServiceTest {

    @Autowired
    private ConferenceTimeRepository conferenceTimeRepository;

    @Autowired
    private PairRepository pairRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private PairService pairService;

    private List<Attendance> yesterdayAttendances;
    private Pair yesterdayPair;

    private Pair pairOne;
    private Pair pairTwo;

    private ConferenceTime timeSix;
    private ConferenceTime timeTen;

    @BeforeEach
    void setUp() {
        pairRepository.deleteAll();
        attendanceRepository.deleteAll();
        conferenceTimeRepository.deleteAll();
        testEntityManager.flush();
        testEntityManager.clear();
        prepare();

        pairService = new PairService(conferenceTimeRepository, pairRepository, attendanceRepository);

        pairOne.getAttendances().forEach(attendanceRepository::save);
        pairTwo.getAttendances().forEach(attendanceRepository::save);

        pairOne.toGroupedPairs().forEach(pairRepository::save);
        pairTwo.toGroupedPairs().forEach(pairRepository::save);

        yesterdayAttendances.forEach(attendanceRepository::save);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    private void prepare() {
        timeSix = conferenceTimeRepository.save(new ConferenceTime(LocalTime.of(18, 00)));
        timeTen = conferenceTimeRepository.save(new ConferenceTime(LocalTime.of(22, 00)));

        Attendance neozal = new Attendance(TODAY, new Member("손너잘"), timeSix);
        Attendance whyguy = new Attendance(TODAY, new Member("웨지"), timeSix);
        Attendance danijani = new Attendance(TODAY, new Member("다니"), timeSix);
        Attendance soulg = new Attendance(TODAY, new Member("솔지"), timeSix);
        Attendance chu = new Attendance(TODAY, new Member("피카"), timeSix);
        Attendance duck = new Attendance(TODAY, new Member("조연우"), timeSix);

        yesterdayAttendances = List.of(
                new Attendance(YESTERDAY, neozal.getMember(), timeSix),
                new Attendance(YESTERDAY, danijani.getMember(), timeSix),
                new Attendance(YESTERDAY, whyguy.getMember(), timeSix)
        );

        yesterdayPair = new Pair(1L, yesterdayAttendances);

        pairOne = new Pair(1L, List.of(neozal, whyguy, duck));
        pairTwo = new Pair(2L, List.of(danijani, soulg, chu));
    }

    @DisplayName("페어를 반환한다.")
    @Test
    void getPairsByDateAndTime() {
        List<PairResponseDto> pairResponse = pairService.getPairsByDateAndTime(
                TODAY,
                timeSix.getId(),
                LocalDateTime.of(
                        TODAY,
                        LocalTime.of(19, 0)
                )
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
                timeSix.getId(),
                LocalDateTime.of(
                        TODAY,
                        LocalTime.of(19, 0)
                )
        );

        List<PairResponseDto> twice = pairService.getPairsByDateAndTime(
                TODAY,
                timeSix.getId(),
                LocalDateTime.of(
                        TODAY,
                        LocalTime.of(19, 0)
                )
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
                timeSix.getId(),
                LocalDateTime.of(
                        TODAY,
                        LocalTime.of(19, 0)
                )
        );

        List<String> actual = pairs.stream()
                .flatMap(p -> p.getPair().stream())
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
                        TODAY.plusDays(1),
                        timeSix.getId(),
                        LocalDateTime.of(
                                TODAY,
                                LocalTime.of(19, 0)
                        )
                )
        ).isInstanceOf(InvalidDateException.class);
    }

    @DisplayName("회고 시간 전에 매칭을 요청하면 예외")
    @ParameterizedTest
    @CsvSource({"2, 21", "1, 17", "2, 17"})
    void getPairsByDateAndTimeFailWithBadTime(long requestTimeId, int currentTime) {
        assertThatThrownBy(
                () -> pairService.getPairsByDateAndTime(
                        TODAY,
                        requestTimeId,
                        LocalDateTime.of(
                                TODAY,
                                LocalTime.of(currentTime, 0)
                        )
                )
        ).isInstanceOf(InvalidTimeException.class);
    }

    @DisplayName("잘못된 회고 시간을 줄 경우 예외")
    @Test
    void getPairsByDateAndTimeFailWithBadConferenceTime() {
        assertThatThrownBy(
                () -> pairService.getPairsByDateAndTime(
                        TODAY,
                        timeSix.getId(),
                        LocalDateTime.of(
                                TODAY,
                                LocalTime.of(1, 0)
                        )
                )
        ).isInstanceOf(InvalidTimeException.class);
    }
}
