package wooteco.retrospective.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.retrospective.common.Fixture;
import wooteco.retrospective.dao.attendance.AttendanceDao;
import wooteco.retrospective.dao.attendance.TimeDao;
import wooteco.retrospective.dao.member.MemberDao;
import wooteco.retrospective.dao.pair.PairDaoImpl;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.member.ShuffledAttendances;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class pairDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PairDaoImpl pairDao;
    private MemberDao memberDao;
    private AttendanceDao attendanceDao;
    private TimeDao timeDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        timeDao = new TimeDao(jdbcTemplate);
        attendanceDao = new AttendanceDao(jdbcTemplate, memberDao, timeDao);
        pairDao = new PairDaoImpl(jdbcTemplate);

        Member neozal = memberDao.insert(Fixture.neozal.getMember());
        Member whyguy = memberDao.insert(Fixture.whyguy.getMember());
        Member danijani = memberDao.insert(Fixture.danijani.getMember());
        Member duck = memberDao.insert(Fixture.duck.getMember());

        Time timeSix = timeDao.findById(1L).orElseThrow(RuntimeException::new);
        attendanceDao.insert(new Attendance(LocalDate.now(), neozal, timeSix));
        attendanceDao.insert(new Attendance(LocalDate.now(), whyguy, timeSix));
        attendanceDao.insert(new Attendance(LocalDate.now(), danijani, timeSix));
        attendanceDao.insert(new Attendance(LocalDate.now(), duck, timeSix));

        Time timeTen = timeDao.findById(2L).orElseThrow(RuntimeException::new);
        attendanceDao.insert(new Attendance(LocalDate.now(), neozal, timeTen));
        attendanceDao.insert(new Attendance(LocalDate.now(), whyguy, timeTen));
    }

    @AfterEach
    void cleanUp() {
        jdbcTemplate.update("DELETE FROM PAIR");
        jdbcTemplate.update("DELETE FROM ATTENDANCE");
        jdbcTemplate.update("DELETE FROM MEMBER");

        jdbcTemplate.update("ALTER TABLE MEMBER ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("ALTER TABLE ATTENDANCE ALTER COLUMN id RESTART WITH 1");
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

        Pairs actual = pairDao.findByDateAndTime(LocalDate.now(), new Time(id, time))
                .orElseThrow(RuntimeException::new);

        assertThat(actual.getPairs()).isEqualTo(pairs.getPairs());
    }

    private Pairs getPairsAt(LocalTime time) {
        List<Attendance> attendances = attendanceDao.findByDate(
                LocalDate.now()
        );

        List<Attendance> attendancesAtTime = attendances.stream()
                .filter(attendance -> attendance.getTime().equals(new Time(time)))
                .collect(toList());

        return Pairs.withDefaultMatchPolicy(new ShuffledAttendances(attendancesAtTime, i -> i));
    }
}
