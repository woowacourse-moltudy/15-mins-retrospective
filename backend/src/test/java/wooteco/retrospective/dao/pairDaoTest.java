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
import wooteco.retrospective.dao.pair.PairDao;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.member.ShuffledMembers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
public class pairDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private PairDao pairDao;
    private MemberDao memberDao;
    private AttendanceDao attendanceDao;
    private TimeDao timeDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        timeDao = new TimeDao(jdbcTemplate);
        attendanceDao = new AttendanceDao(jdbcTemplate, memberDao, timeDao);
        pairDao = new PairDao(attendanceDao, memberDao, jdbcTemplate);

        Member neozal = memberDao.insert(Fixture.neozal);
        Member whyguy = memberDao.insert(Fixture.whyguy);
        Member danijani = memberDao.insert(Fixture.danijani);
        Member duck = memberDao.insert(Fixture.duck);

        Time timeSix = timeDao.findById(1L).orElseThrow(RuntimeException::new);
        attendanceDao.insert(new Attendance(LocalDateTime.now(), neozal, timeSix));
        attendanceDao.insert(new Attendance(LocalDateTime.now(), whyguy, timeSix));
        attendanceDao.insert(new Attendance(LocalDateTime.now(), danijani, timeSix));
        attendanceDao.insert(new Attendance(LocalDateTime.now(), duck, timeSix));

        Time timeTen = timeDao.findById(2L).orElseThrow(RuntimeException::new);
        attendanceDao.insert(new Attendance(LocalDateTime.now(), neozal, timeTen));
        attendanceDao.insert(new Attendance(LocalDateTime.now(), whyguy, timeTen));
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
        Pairs pairs = getPairsAt(6);
        pairDao.insert(pairs);

        Long count = jdbcTemplate.queryForObject("SELECT count(*) FROM PAIR", Long.class);
        assertThat(count).isEqualTo(4);
    }

    @DisplayName("날짜와 시간으로 페어를 검색한다.")
    @ParameterizedTest
    @CsvSource({"1, 6", "2, 10"})
    void findByDateAndTime(long id, int time) {
        Pairs pairs = getPairsAt(time);
        pairDao.insert(pairs);

        Pairs actual = pairDao.findByDateAndTime(LocalDateTime.now(), new Time(id, 6));
        assertThat(actual.getPairs()).isEqualTo(pairs.getPairs());
    }

    private Pairs getPairsAt(int time) {
        List<Attendance> attendances = attendanceDao.findByDate(
                LocalDateTime.now()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        List<Member> members = attendances.stream()
                .filter(attendance -> attendance.getTime().equals(new Time(time)))
                .map(Attendance::getMember)
                .collect(toList());

        return Pairs.withDefaultMatchPolicy(new ShuffledMembers(members, i -> i));
    }
}
