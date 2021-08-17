package wooteco.retrospective.application.pair;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.repository.AttendanceRepository;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.conference_time.repository.ConferenceTimeRepository;
import wooteco.retrospective.domain.pair.GroupedAttendance;
import wooteco.retrospective.domain.pair.Pair;
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
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;
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

        Map<Long, List<GroupedAttendance>> groupedPairs = pairRepository.findByDateAndConferenceTime(date, conferenceTime)
                .stream()
                .collect(groupingBy(GroupedAttendance::getGroupId));

        List<PairResponseDto> pairResponseDtos = groupedPairs.entrySet().stream()
                .map(entry -> new Pair(entry.getKey(), toAttendances(entry.getValue())))
                .map(PairResponseDto::new)
                .collect(toList());

        if(pairResponseDtos.isEmpty()) {
            return createNewPairAndReturnPairResponseDtoAt(date, conferenceTime);
        }

        return pairResponseDtos;
    }

    private List<Attendance> toAttendances(List<GroupedAttendance> groupedAttendances) {
        return groupedAttendances.stream()
                .map(GroupedAttendance::getAttendance)
                .collect(toList());
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

        List<GroupedAttendance> groupedAttendances = pairs.getPairs().stream()
                .flatMap(pair -> pair.toGroupedPairs().stream())
                .map(pairRepository::save)
                .collect(toList());

        Map<Long, List<GroupedAttendance>> groupedPairsByGroupId = groupedAttendances.stream()
                .collect(groupingBy(GroupedAttendance::getGroupId));

        return groupedPairsByGroupId.entrySet().stream()
                .map(entry -> new Pair(entry.getKey(), toAttendances(entry.getValue())))
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<Attendance> getAttendancesOf(LocalDate date, ConferenceTime conferenceTime) {
        return attendanceRepository.findAttendanceByDateAndConferenceTime(date, conferenceTime);
    }
}
