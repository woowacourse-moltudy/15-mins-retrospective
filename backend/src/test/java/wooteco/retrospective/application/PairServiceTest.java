package wooteco.retrospective.application;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import wooteco.retrospective.application.dto.MemberResponseDto;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.application.pair.PairService;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.AttendanceRepository;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.attendance.ConferenceTimeRepository;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.PairRepository;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.exception.InvalidDateException;
import wooteco.retrospective.exception.InvalidTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static wooteco.retrospective.common.Fixture.*;

@ExtendWith(MockitoExtension.class)
class PairServiceTest {

    @Mock
    private ConferenceTimeRepository conferenceTimeRepository;
    @Mock
    private PairRepository pairRepository;
    @Mock
    private AttendanceRepository attendanceRepository;

    @InjectMocks
    private PairService pairService;

    private static final List<Attendance> yesterdayAttendances = List.of(
            new Attendance(1L, YESTERDAY, neozal.getMember(), new ConferenceTime(TIME_SIX)),
            new Attendance(2L, YESTERDAY, danijani.getMember(), new ConferenceTime(TIME_SIX)),
            new Attendance(3L, YESTERDAY, whyguy.getMember(), new ConferenceTime(TIME_SIX))
    );
    private static final Pair yesterdayPair = new Pair(1L, yesterdayAttendances);

    @DisplayName("페어를 반환한다.")
    @Test
    void getPairsByDateAndTime() {
        List<Attendance> attendances = List.of(neozal, whyguy, duck, danijani, soulg, chu);
        List<Pair> pairs = Pairs.withDefaultMatchPolicy(attendances).getPairs();

        given(conferenceTimeRepository.findById(anyLong()))
                .willReturn(Optional.of(new ConferenceTime(1L, LocalTime.of(18, 0))));
        given(attendanceRepository.findAttendancesByDateAndConferenceTimeIdAndPairNotNull(any(LocalDate.class), anyLong()))
                .willReturn(Collections.emptyList());
        given(attendanceRepository.findAttendancesByDateAndConferenceTime(any(LocalDate.class), any(ConferenceTime.class)))
                .willReturn(attendances);
        given(pairRepository.saveAll(any()))
                .willReturn(pairs);

        List<PairResponseDto> pairResponse = pairService
                .getPairsByDateAndTime(
                        TODAY,
                        1L,
                        LocalDateTime.of(TODAY, LocalTime.of(19, 0))
                );

        List<PairResponseDto> expected = pairs.stream()
                .map(PairResponseDto::new)
                .collect(toList());

        assertThat(pairResponse)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("페어를 두번 요청해도 동일한 값이 반환된다.")
    @Test
    void getPairsByDateAndTimeWithTwice() {
        List<Attendance> attendances = List.of(neozal, whyguy, duck, danijani, soulg, chu);
        List<Pair> pairs = Pairs.withDefaultMatchPolicy(attendances).getPairs();

        given(conferenceTimeRepository.findById(anyLong()))
                .willReturn(Optional.of(new ConferenceTime(1L, LocalTime.of(18, 0))));
        given(attendanceRepository.findAttendancesByDateAndConferenceTimeIdAndPairNotNull(any(LocalDate.class), anyLong()))
                .willReturn(Collections.emptyList());
        given(attendanceRepository.findAttendancesByDateAndConferenceTime(any(LocalDate.class), any(ConferenceTime.class)))
                .willReturn(attendances);
        given(pairRepository.saveAll(any()))
                .willReturn(pairs);

        List<PairResponseDto> once = pairService.getPairsByDateAndTime(
                TODAY,
                1L,
                LocalDateTime.of(TODAY, LocalTime.of(19, 0))
        );

        List<PairResponseDto> twice = pairService.getPairsByDateAndTime(
                TODAY,
                1L,
                LocalDateTime.of(TODAY, LocalTime.of(19, 0))
        );

        assertThat(once)
                .usingRecursiveComparison()
                .isEqualTo(twice);
    }

    @DisplayName("날짜가 지난 회고의 페어를 요청하면 반환한다.")
    @Test
    void getPairsByDateAndTimeWithBeforeDays() {
        List<Attendance> attendances = List.of(neozal, whyguy, duck, danijani, soulg, chu);

        given(conferenceTimeRepository.findById(anyLong()))
                .willReturn(Optional.of(new ConferenceTime(1L, LocalTime.of(18, 0))));
        given(attendanceRepository.findAttendancesByDateAndConferenceTimeIdAndPairNotNull(any(LocalDate.class), anyLong()))
                .willReturn(Collections.emptyList());
        given(attendanceRepository.findAttendancesByDateAndConferenceTime(any(LocalDate.class), any(ConferenceTime.class)))
                .willReturn(attendances);
        given(pairRepository.saveAll(any()))
                .willReturn(Collections.singletonList(yesterdayPair));

        List<PairResponseDto> pairs = pairService.getPairsByDateAndTime(
                YESTERDAY,
                1L,
                LocalDateTime.of(TODAY, LocalTime.of(19, 0))
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
                        1L,
                        LocalDateTime.of(TODAY, LocalTime.of(19, 0))
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
                        LocalDateTime.of(TODAY, LocalTime.of(currentTime, 0))
                )
        ).isInstanceOf(InvalidTimeException.class);
    }

    @DisplayName("잘못된 회고 시간을 줄 경우 예외")
    @Test
    void getPairsByDateAndTimeFailWithBadConferenceTime() {
        assertThatThrownBy(
                () -> pairService.getPairsByDateAndTime(
                        TODAY,
                        1L,
                        LocalDateTime.of(TODAY, LocalTime.of(1, 0))
                )
        ).isInstanceOf(InvalidTimeException.class);
    }
}
