package wooteco.retrospective.infrastructure.dao.attendance;

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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
        Attendance newAttendance = insertAttendance();
        assertThat(newAttendance.getMember()).isEqualTo(MEMBER_SALLY);
        assertThat(newAttendance.getTime()).isEqualTo(TIME_SIX);
    }

    @Test
    @DisplayName("출석부를 조회한다.")
    void findById() {
        insertAttendance();
        Attendance expectedAttendance = new Attendance(
                1L,
                LocalDateTime.now(),
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

        LocalDateTime now = LocalDateTime.now();
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
        Attendance attendance = new Attendance(madeMember, time);

        return attendanceDao.insert(attendance);
    }
}
