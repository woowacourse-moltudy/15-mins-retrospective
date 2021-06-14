package wooteco.retrospective.infrastructure.dao.attendance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;

@JdbcTest
@Sql("classpath:recreate-schema.sql")
class AttendanceDaoTest {
    private static final Member MEMBER_SALLY = new Member("sally");
    private static final Time TIME_SIX = new Time(1L, LocalTime.of(18, 0, 0));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private TimeDao timeDao;
    private AttendanceDao attendanceDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        timeDao = new TimeDao(jdbcTemplate);
        attendanceDao = new AttendanceDao(jdbcTemplate, memberDao, timeDao);
    }

    @DisplayName("출석부를 추가한다.")
    @Test
    void insert() {
        Attendance expectedAttendance = new Attendance(LocalDate.now(), MEMBER_SALLY, TIME_SIX);
        Attendance newAttendance = insertAttendance();

        assertThat(expectedAttendance).isEqualTo(newAttendance);
    }

    @DisplayName("출석부를 조회한다.")
    @Test
    void findById() {
        insertAttendance();
        Attendance expectedAttendance = new Attendance(
            1L,
            LocalDate.now(),
            MEMBER_SALLY,
            TIME_SIX
        );
        Attendance attendance = attendanceDao.findById(1L);

        assertThat(expectedAttendance).isEqualTo(attendance);
    }

    @DisplayName("같은 시간에 같은 멤버가 있는지 조회한다.")
    @Test
    void existSameTime() {
        insertAttendance();
        Attendance attendance = attendanceDao.findById(1L);

        assertThat(attendanceDao.isExistSameTime(
            LocalDate.now(),
            attendance.getMemberId(),
            attendance.getTimeId()
        )).isTrue();
    }

    @DisplayName("다른 날짜에 같은 멤버가 같은 시간에 있는지 조회한다.")
    @Test
    void existSameTimeException() {
        insertAttendance();
        Attendance attendance = attendanceDao.findById(1L);

        assertThat(attendanceDao.isExistSameTime(
            LocalDate.now().minusDays(1),
            attendance.getMemberId(),
            attendance.getTimeId()
        )).isFalse();
    }

    @DisplayName("날짜에 따른 출석부를 조회한다.")
    @Test
    void findByDate() {
        insertAttendance();

        LocalDate now = LocalDate.now();
        Attendance expectedAttendance = new Attendance(
            1L,
            now,
            MEMBER_SALLY,
            TIME_SIX
        );

        List<Attendance> attendance = attendanceDao.findByDate(now);

        assertThat(attendance.contains(expectedAttendance)).isTrue();
    }

    @DisplayName("날짜, 시간에 따른 출석부를 조회한다.")
    @Test
    void findByTime() {
        insertAttendance();

        LocalDate now = LocalDate.now();
        Attendance expectedAttendance = new Attendance(
            1L,
            now,
            MEMBER_SALLY,
            TIME_SIX
        );

        List<Attendance> attendance = attendanceDao.findByTime(now, 1L);

        assertThat(attendance.contains(expectedAttendance)).isTrue();
    }

    @DisplayName("멤버, 시간대에 따른 출석부를 삭제한다.")
    @Test
    void delete() {
        Attendance attendance = insertAttendance();

        int deleteCount = attendanceDao.delete(attendance);
        assertThat(deleteCount).isEqualTo(1);
    }

    private Attendance insertAttendance() {
        Member madeMember = memberDao.insert(MEMBER_SALLY);
        Time time = timeDao.findById(1L).orElseThrow(RuntimeException::new);
        Attendance attendance = new Attendance(LocalDate.now(), madeMember, time);

        return attendanceDao.insert(attendance);
    }
}
