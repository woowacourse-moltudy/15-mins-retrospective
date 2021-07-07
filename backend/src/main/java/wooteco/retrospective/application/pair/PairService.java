package wooteco.retrospective.application.pair;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.AttendanceRepository;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.attendance.ConferenceTimeRepository;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.PairRepository;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.member.ShuffledAttendances;
import wooteco.retrospective.exception.InvalidConferenceTimeException;
import wooteco.retrospective.exception.InvalidDateException;
import wooteco.retrospective.exception.InvalidTimeException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
public class PairService {

    private final ConferenceTimeRepository conferenceTimeRepository;
    private final PairRepository pairRepository;
    private final AttendanceRepository attendanceRepository;

    public PairService(ConferenceTimeRepository conferenceTimeRepository,
                       PairRepository pairRepository,
                       AttendanceRepository attendanceRepository) {
        this.conferenceTimeRepository = conferenceTimeRepository;
        this.pairRepository = pairRepository;
        this.attendanceRepository = attendanceRepository;
    }

    @Transactional
    public List<PairResponseDto> getPairsByDateAndTime(LocalDate date,
                                                       Long conferenceTimeId,
                                                       LocalDateTime currentDateTime) {
        Logger logger = LoggerFactory.getLogger(this.getClass());
        logger.info(String.valueOf(conferenceTimeRepository.hashCode()));

        validateIsRightDate(date, currentDateTime.toLocalDate());

        ConferenceTime conferenceTime = findConferenceTimeBy(conferenceTimeId);
        validateCallableConferenceTime(conferenceTime, currentDateTime.toLocalTime());

        List<Attendance> attendances = getAttendancesOf(date, conferenceTimeId);
        if (attendances.isEmpty()) {
            return createNewPairAndReturnPairResponseDtoAt(date, conferenceTime);
        }

        List<Pair> pairs = getPairs(date, conferenceTime);

        return toPairResponseDtos(Pairs.from(pairs));
    }

    private List<Pair> getPairs(LocalDate date, ConferenceTime conferenceTime) {
        return pairRepository.
                findPairsByDateAndTime(date, conferenceTime.getConferenceTime());
    }

    private List<Attendance> getAttendancesOf(LocalDate date, Long conferenceTimeId) {
        return attendanceRepository
                .findAttendancesByDateAndConferenceTimeIdAndPairNotNull(date, conferenceTimeId);
    }

    private ConferenceTime findConferenceTimeBy(Long conferenceTimeId) {
        return conferenceTimeRepository
                .findById(conferenceTimeId)
                .orElseThrow(InvalidConferenceTimeException::new);
    }

    private void validateCallableConferenceTime(ConferenceTime conferenceTime, LocalTime currentTime) {
        if (!conferenceTime.isBefore(currentTime)) {
            throw new InvalidTimeException();
        }
    }

    private void validateIsRightDate(LocalDate date, LocalDate currentDate) {
        if (date.isAfter(currentDate)) {
            throw new InvalidDateException();
        }
    }

    private List<PairResponseDto> toPairResponseDtos(Pairs pairs) {
        return pairs.getPairs().stream()
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<PairResponseDto> createNewPairAndReturnPairResponseDtoAt(LocalDate date, ConferenceTime conferenceTime) {
        List<Attendance> attendances = getAttendancesOf(date, conferenceTime);

        Pairs pairs = Pairs.withDefaultMatchPolicy(new ShuffledAttendances(attendances));

        return pairRepository.saveAll(pairs.getPairs()).stream()
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<Attendance> getAttendancesOf(LocalDate date, ConferenceTime conferenceTime) {
        return attendanceRepository
                .findAttendancesByDateAndConferenceTime(date, conferenceTime);
    }
}
