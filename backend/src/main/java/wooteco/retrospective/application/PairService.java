package wooteco.retrospective.application;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.application.dto.PairResponseDto;
import wooteco.retrospective.dao.attendance.AttendanceDao;
import wooteco.retrospective.dao.attendance.TimeDao;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.dao.PairDao;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.member.ShuffledMembers;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
    public List<PairResponseDto> getPairsByDateAndTime(LocalDateTime date, int requestedTime) {
        Time time = getTime(requestedTime);
        validateIsRightTime(time);
        validateIsRightDate(date);

        return pairDao.findByDateAndTime(date, time)
                .map(toPairResponseDtos())
                .orElseGet(() -> createNewPairAndReturnPairResponseDtoAt(date, time));
    }

    private Time getTime(int time) {
        return timeDao.findAll().stream()
                .filter(t -> t.getTime() == time)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private void validateIsRightTime(Time requestedTime) {
        timeDao.findAll().stream()
                .filter(time -> time.getTime() <= LocalTime.now().getHour())
                .filter(time -> time.getTime() == requestedTime.getTime())
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private void validateIsRightDate(LocalDateTime date) {
        if (date.isAfter(LocalDateTime.now())) {
            throw new RuntimeException();
        }
    }

    private Function<Pairs, List<PairResponseDto>> toPairResponseDtos() {
        return pairs -> pairs.getPairs().stream()
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<PairResponseDto> createNewPairAndReturnPairResponseDtoAt(LocalDateTime date, Time time) {
        List<Member> members = getMembersOf(date, time);

        Pairs pairs = Pairs.withDefaultMatchPolicy(new ShuffledMembers(members));

        return pairDao.insert(pairs).getPairs().stream()
                .map(PairResponseDto::new)
                .collect(toList());
    }

    private List<Member> getMembersOf(LocalDateTime date, Time time) {
        return attendanceDao.findByDate(date.format(DateTimeFormatter.ofPattern("yyyy-DD-mm"))).stream()
                .filter(attendance -> attendance.getTime().equals(time))
                .map(Attendance::getMember)
                .collect(toList());
    }
}
