package wooteco.retrospective.application.pair;

import static java.util.stream.Collectors.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.domain.dao.AttendanceDao;
import wooteco.retrospective.domain.dao.ConferenceTimeDao;
import wooteco.retrospective.domain.dao.PairDao;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.member.ShuffledAttendances;
import wooteco.retrospective.exception.InvalidConferenceTimeException;
import wooteco.retrospective.exception.InvalidDateException;
import wooteco.retrospective.exception.InvalidTimeException;

@Transactional(readOnly = true)
@Service
public class PairService {

    private final ConferenceTimeDao conferenceTimeDao;
    private final PairDao pairDao;
    private final AttendanceDao attendanceDao;

    public PairService(ConferenceTimeDao conferenceTimeDao, PairDao pairDao, AttendanceDao attendanceDao) {
        this.conferenceTimeDao = conferenceTimeDao;
        this.pairDao = pairDao;
        this.attendanceDao = attendanceDao;
    }

    @Transactional
    public List<PairResponseDto> getPairsByDateAndTime(LocalDate date,
        LocalTime requestedConferenceTime,
        LocalTime currentTime) {
        ConferenceTime conferenceTime = getConferenceTimeIfValid(new ConferenceTime(requestedConferenceTime));
        validateIsCallableTime(conferenceTime, new ConferenceTime(currentTime));
        validateIsRightDate(date);

        return pairDao.findByDateAndTime(date, conferenceTime)
            .map(toPairResponseDtos())
            .orElseGet(() -> createNewPairAndReturnPairResponseDtoAt(date, conferenceTime));
    }

    private ConferenceTime getConferenceTimeIfValid(ConferenceTime conferenceTime) {
        return conferenceTimeDao.findAll().stream()
            .filter(time -> time.equals(conferenceTime))
            .findAny()
            .orElseThrow(InvalidConferenceTimeException::new);
    }

    private void validateIsCallableTime(ConferenceTime conferenceTime, ConferenceTime currentTime) {
        conferenceTimeDao.findAll().stream()
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
        ConferenceTime conferenceTime) {
        List<Attendance> attendances = getAttendancesOf(date, conferenceTime);

        Pairs pairs = Pairs.withDefaultMatchPolicy(new ShuffledAttendances(attendances));

        return pairDao.insert(pairs).getPairs().stream()
            .map(PairResponseDto::new)
            .collect(toList());
    }

    private List<Attendance> getAttendancesOf(LocalDate date, ConferenceTime conferenceTime) {
        return attendanceDao.findByDate(date).stream()
            .filter(attendance -> attendance.isAttendAt(conferenceTime))
            .collect(toList());
    }
}
