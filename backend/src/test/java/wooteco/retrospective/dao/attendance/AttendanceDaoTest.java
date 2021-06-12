package wooteco.retrospective.dao.attendance;

import static org.assertj.core.api.Assertions.*;

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
        Attendance expectedAttendance = new Attendance(1L, new Timestamp(System.currentTimeMillis()), MEMBER_SALLY,
            TIME_SIX);
        Attendance attendance = attendanceDao.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(expectedAttendance).isEqualTo(attendance);
    }

    @Test
    @DisplayName("같은 시간에 같은 멤버가 있는지 조회한다.")
    void existSameTime() {
        insertAttendance();
        Attendance attendance = attendanceDao.findById(1L).orElseThrow(RuntimeException::new);

        assertThat(attendanceDao.isExistSameTime(
                    attendance.getMemberId(),
                    attendance.getTimeId()
                )).isTrue();
    }

    @Test
    @DisplayName("날짜에 따른 출석부를 조회한다.")
    void findByDate() {
        insertAttendance();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        Attendance expectedAttendance = new Attendance(1L, now, MEMBER_SALLY, TIME_SIX);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(now);

        List<Attendance> attendance = attendanceDao.findByDate(date);

        assertThat(attendance.contains(expectedAttendance)).isTrue();
    }

    private Attendance insertAttendance() {
        Member madeMember = memberDao.insert(MEMBER_SALLY);
        Time time = timeDao.findById(1L).orElseThrow(RuntimeException::new);
        Attendance attendance = new Attendance(madeMember, time);

        return attendanceDao.insert(attendance);
    }
}