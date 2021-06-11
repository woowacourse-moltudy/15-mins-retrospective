package wooteco.retrospective.dao.attendance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import wooteco.retrospective.dao.member.MemberDao;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;

@JdbcTest
@Sql("classpath:test-schema.sql")
class AttendanceDaoTest {
    private static final Member MEMBER = new Member("sally");
    private static final Time TIME = new Time(6);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private AttendanceDao attendanceDao;
    private MemberDao memberDao;
    private TimeDao timeDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        timeDao = new TimeDao(jdbcTemplate);
        attendanceDao = new AttendanceDao(jdbcTemplate, memberDao, timeDao);
    }

    @Test
    @DisplayName("출석부를 추가한다.")
    void insert() {
        Member madeMember = memberDao.insert(MEMBER);
        Time time = timeDao.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(attendanceDao.insert(new Attendance(madeMember, time))).isInstanceOf(Long.class);
    }

    @Test
    @DisplayName("출석부를 조회한다.")
    void findById() {
        insert();
        Attendance expectedAttendance = new Attendance(1L, new Timestamp(System.currentTimeMillis()), MEMBER, TIME);
        Attendance attendance = attendanceDao.findById(1L).orElseThrow(RuntimeException::new);

        assertEquals(expectedAttendance, attendance);
    }

    @Test
    @DisplayName("같은 시간에 같은 멤버가 있는지 조회한다.")
    void existSameTime() {
        insert();
        Attendance attendance = attendanceDao.findById(1L).orElseThrow(RuntimeException::new);

        assertTrue(
            attendanceDao.existSameTime(attendance.getMember().getId(), attendance.getTime().getId()).isPresent());
    }

    @Test
    @DisplayName("날짜에 따른 출석부를 조회한다.")
    void findByDate() {
        insert();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Attendance expectedAttendance = new Attendance(1L, now, MEMBER, TIME);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(now);

        List<Attendance> attendance = attendanceDao.findByDate(date);

        assertTrue(attendance.contains(expectedAttendance));
    }

}