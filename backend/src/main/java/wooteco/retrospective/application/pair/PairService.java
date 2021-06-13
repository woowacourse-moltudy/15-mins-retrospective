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
    public List<PairResponseDto> getPairsByDateAndTime(LocalDate date, LocalTime requestedTime) {
        Time time = getTime(requestedTime);
        validateIsRightTime(time);
        validateIsRightDate(date);

        return pairDao.findByDateAndTime(date, time)
                .map(toPairResponseDtos())
                .orElseGet(() -> createNewPairAndReturnPairResponseDtoAt(date, time));
    }

    private Time getTime(LocalTime time) {
        return timeDao.findAll().stream()
                .filter(t -> t.getTime().equals(time))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private void validateIsRightTime(Time requestedTime) {
        timeDao.findAll().stream()
                .filter(time -> time.getTime().isBefore(LocalTime.now()))
                .filter(time -> time.getTime().equals(requestedTime.getTime()))
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private void validateIsRightDate(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new RuntimeException();
        }
    }

    private Function<Pairs, List<PairResponseDto>> toPairResponseDtos() {
        return pairs -> pairs.getPairs().stream()
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<PairResponseDto> createNewPairAndReturnPairResponseDtoAt(LocalDate date, Time time) {
        List<Attendance> attendances = getAttendancesOf(date, time);

        Pairs pairs = Pairs.withDefaultMatchPolicy(new ShuffledAttendances(attendances));

        return pairDao.insert(pairs).getPairs().stream()
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<Attendance> getAttendancesOf(LocalDate date, Time time) {
        return attendanceDao.findByDate(date).stream()
                .filter(attendance -> attendance.getTime().equals(time))
                .collect(toList());
    }
}
