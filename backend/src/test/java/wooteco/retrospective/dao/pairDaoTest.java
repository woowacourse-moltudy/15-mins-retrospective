package wooteco.retrospective.dao;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.common.Fixture;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.dao.AttendanceDao;
import wooteco.retrospective.domain.dao.ConferenceTimeDao;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.member.ShuffledAttendances;
import wooteco.retrospective.infrastructure.dao.attendance.AttendanceDaoImpl;
import wooteco.retrospective.infrastructure.dao.attendance.ConferenceTimeDaoImpl;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;
import wooteco.retrospective.infrastructure.dao.pair.PairDaoImpl;

@Transactional
@JdbcTest
public class pairDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PairDaoImpl pairDao;
    private MemberDao memberDao;
    private AttendanceDao attendanceDao;
    private ConferenceTimeDao conferenceTimeDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        conferenceTimeDao = new ConferenceTimeDaoImpl(jdbcTemplate);
        attendanceDao = new AttendanceDaoImpl(jdbcTemplate, memberDao, conferenceTimeDao);
        pairDao = new PairDaoImpl(jdbcTemplate);

        Member neozal = memberDao.insert(Fixture.neozal.getMember());
        Member whyguy = memberDao.insert(Fixture.whyguy.getMember());
        Member danijani = memberDao.insert(Fixture.danijani.getMember());
        Member duck = memberDao.insert(Fixture.duck.getMember());

        ConferenceTime timeSix = conferenceTimeDao.findById(1L).orElseThrow(RuntimeException::new);
        attendanceDao.insert(new Attendance(LocalDate.now(), neozal, timeSix));
        attendanceDao.insert(new Attendance(LocalDate.now(), whyguy, timeSix));
        attendanceDao.insert(new Attendance(LocalDate.now(), danijani, timeSix));
        attendanceDao.insert(new Attendance(LocalDate.now(), duck, timeSix));

        ConferenceTime timeTen = conferenceTimeDao.findById(2L).orElseThrow(RuntimeException::new);
        attendanceDao.insert(new Attendance(LocalDate.now(), neozal, timeTen));
        attendanceDao.insert(new Attendance(LocalDate.now(), whyguy, timeTen));
    }

    @DisplayName("페어를 저장한다.")
    @Test
    void insert() {
        Pairs pairs = getPairsAt(LocalTime.of(18, 0));
        pairDao.insert(pairs);

        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM PAIR", Long.class);
        assertThat(count).isEqualTo(4);
    }

    @DisplayName("날짜와 시간으로 페어를 검색한다.")
    @ParameterizedTest
    @CsvSource({"1, 18, 0", "2, 22, 0"})
    void findByDateAndTime(long id, int hour, int minute) {
        LocalTime time = LocalTime.of(hour, minute);
        Pairs pairs = getPairsAt(time);
        pairDao.insert(pairs);

        Pairs actual = pairDao.findByDateAndTime(LocalDate.now(), new ConferenceTime(id, time))
            .orElseThrow(RuntimeException::new);

        assertThat(actual.getPairs()).isEqualTo(pairs.getPairs());
    }

    private Pairs getPairsAt(LocalTime time) {
        List<Attendance> attendances = attendanceDao.findByDate(
            LocalDate.now()
        );

        List<Attendance> attendancesAtTime = attendances.stream()
            .filter(attendance -> attendance.isAttendAt(new ConferenceTime(time)))
            .collect(toList());

        return Pairs.withDefaultMatchPolicy(new ShuffledAttendances(attendancesAtTime, i -> i));
    }
}
