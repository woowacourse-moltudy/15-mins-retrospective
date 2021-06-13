package wooteco.retrospective.dao.attendance;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
@Sql("classpath:recreate-schema.sql")
class AttendanceDaoTest {
    private static final Member MEMBER_SALLY = new Member("sally");
    private static final Time TIME_SIX = new Time(6);

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

    @Test
    @DisplayName("출석부를 추가한다.")
    void insert() {
        Attendance expectedAttendance = new Attendance(LocalDate.now(), MEMBER_SALLY, TIME_SIX);
        Attendance newAttendance = insertAttendance();

        assertThat(expectedAttendance).isEqualTo(newAttendance);
    }

    @Test
    @DisplayName("출석부를 조회한다.")
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

    @Test
    @DisplayName("같은 시간에 같은 멤버가 있는지 조회한다.")
    void existSameTime() {
        insertAttendance();
        Attendance attendance = attendanceDao.findById(1L);

        assertThat(attendanceDao.isExistSameTime(
            attendance.getMemberId(),
            attendance.getTimeId()
        )).isTrue();
    }

    @Test
    @DisplayName("날짜에 따른 출석부를 조회한다.")
    void findByDate() {
        insertAttendance();

        LocalDate now = LocalDate.now();
        Attendance expectedAttendance = new Attendance(
            1L,
            now,
            MEMBER_SALLY,
            TIME_SIX
        );

        List<Attendance> attendance = attendanceDao.findByDate(
            now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        );

        assertThat(attendance.contains(expectedAttendance)).isTrue();
    }

    private Attendance insertAttendance() {
        Member madeMember = memberDao.insert(MEMBER_SALLY);
        Time time = timeDao.findById(1L).orElseThrow(RuntimeException::new);
        Attendance attendance = new Attendance(LocalDate.now(), madeMember, time);

        return attendanceDao.insert(attendance);
    }
}