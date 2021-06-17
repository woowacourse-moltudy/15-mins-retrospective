package wooteco.retrospective.application.pair;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.dao.AttendanceDao;
import wooteco.retrospective.domain.dao.PairDao;
import wooteco.retrospective.domain.dao.TimeDao;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.member.ShuffledAttendances;
import wooteco.retrospective.exception.InvalidConferenceTimeException;
import wooteco.retrospective.exception.InvalidDateException;
import wooteco.retrospective.exception.InvalidTimeException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

@Transactional(readOnly = true)
@Service
public class PairService {

    private final TimeDao timeDao;
    private final PairDao pairDao;
    private final AttendanceDao attendanceDao;

    public PairService(TimeDao timeDao, PairDao pairDao, AttendanceDao attendanceDao) {
        this.timeDao = timeDao;
        this.pairDao = pairDao;
        this.attendanceDao = attendanceDao;
    }

    @Transactional
    public List<PairResponseDto> getPairsByDateAndTime(LocalDate date,
                                                       LocalTime requestedConferenceTime,
                                                       LocalTime currentTime) {
        Time conferenceTime = getConferenceTimeIfValid(new Time(requestedConferenceTime));
        validateIsCallableTime(conferenceTime, new Time(currentTime));
        validateIsRightDate(date);

        return pairDao.findByDateAndTime(date, conferenceTime)
                .map(toPairResponseDtos())
                .orElseGet(() -> createNewPairAndReturnPairResponseDtoAt(date, conferenceTime));
    }

    private Time getConferenceTimeIfValid(Time conferenceTime) {
        return timeDao.findAll().stream()
                .filter(time -> time.equals(conferenceTime))
                .findAny()
                .orElseThrow(InvalidConferenceTimeException::new);
    }

    private void validateIsCallableTime(Time conferenceTime, Time currentTime) {
        timeDao.findAll().stream()
                .filter(time -> time.isBefore(currentTime))
                .filter(time -> time.equals(conferenceTime))
                .findAny()
                .orElseThrow(InvalidTimeException::new);
    }

    private void validateIsRightDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new InvalidDateException();
        }
    }

    private Function<Pairs, List<PairResponseDto>> toPairResponseDtos() {
        return pairs -> pairs.getPairs().stream()
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<PairResponseDto> createNewPairAndReturnPairResponseDtoAt(LocalDate date,
                                                                          Time conferenceTime) {
        List<Attendance> attendances = getAttendancesOf(date, conferenceTime);

        Pairs pairs = Pairs.withDefaultMatchPolicy(new ShuffledAttendances(attendances));

        return pairDao.insert(pairs).getPairs().stream()
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<Attendance> getAttendancesOf(LocalDate date, Time conferenceTime) {
        return attendanceDao.findByDate(date).stream()
                .filter(attendance -> attendance.isAttendAt(conferenceTime))
                .collect(toList());
    }
}
