package wooteco.retrospective.application.pair;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.repository.AttendanceRepository;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.conference_time.repository.ConferenceTimeRepository;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.member.ShuffledAttendances;
import wooteco.retrospective.domain.pair.repository.PairRepository;
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
        validateIsRightDate(date, currentDateTime.toLocalDate());

        ConferenceTime conferenceTime = conferenceTimeRepository.findById(conferenceTimeId)
                .orElseThrow(InvalidConferenceTimeException::new);

        isCallableTime(conferenceTime, currentDateTime.toLocalTime());

        List<PairResponseDto> pairResponseDtos = pairRepository.findByDateAndConferenceTime(date, conferenceTime)
                .stream()
                .map(PairResponseDto::new)
                .collect(toList());

        if(pairResponseDtos.isEmpty()) {
            return createNewPairAndReturnPairResponseDtoAt(date, conferenceTime);
        }

        return pairResponseDtos;
    }

    private void validateIsRightDate(LocalDate date, LocalDate currentDate) {
        if (date.isAfter(currentDate)) {
            throw new InvalidDateException();
        }
    }

    private void isCallableTime(ConferenceTime conferenceTime, LocalTime currentTime) {
        if(!conferenceTime.isBefore(currentTime)) {
            throw new InvalidTimeException();
        }
    }

    private List<PairResponseDto> createNewPairAndReturnPairResponseDtoAt(LocalDate date,
                                                                          ConferenceTime conferenceTime) {
        List<Attendance> attendances = getAttendancesOf(date, conferenceTime);

        Pairs pairs = Pairs.withDefaultMatchPolicy(new ShuffledAttendances(attendances));

        return pairs.getPairs().stream()
                .map(pairRepository::save)
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<Attendance> getAttendancesOf(LocalDate date, ConferenceTime conferenceTime) {
        return attendanceRepository.findAttendanceByDateAndConferenceTime(date, conferenceTime);
    }
}
