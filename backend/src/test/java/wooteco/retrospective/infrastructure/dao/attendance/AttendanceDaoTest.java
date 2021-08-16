package wooteco.retrospective.infrastructure.dao.attendance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.dao.AttendanceDao;
import wooteco.retrospective.domain.dao.ConferenceTimeDao;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.exception.NotFoundTimeException;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;

@JdbcTest
@Sql("classpath:recreate-schema.sql")
class AttendanceDaoTest {
    private static final Member MEMBER_SALLY = new Member(1L, "sally");
    private static final ConferenceTime CONFERENCE_TIME_SIX = new ConferenceTime(1L, LocalTime.of(18, 0, 0));

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private MemberDao memberDao;
    private ConferenceTimeDao conferenceTimeDao;
    private AttendanceDao attendanceDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        conferenceTimeDao = new ConferenceTimeDaoImpl(jdbcTemplate);
        attendanceDao = new AttendanceDaoImpl(jdbcTemplate, memberDao, conferenceTimeDao);
    }

    @Test
    @DisplayName("출석부를 추가한다.")
    void insert() {
        Attendance expectedAttendance = new Attendance(LocalDate.now(), MEMBER_SALLY, CONFERENCE_TIME_SIX);
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
            CONFERENCE_TIME_SIX
        );
        Attendance attendance = attendanceDao.findById(1L);

        assertThat(expectedAttendance).isEqualTo(attendance);
    }

    @Test
    @DisplayName("같은 시간에 같은 멤버가 있는지 조회한다.")
    void existSameTime() {
        insertAttendance();

        Attendance attendance = attendanceDao.findById(1L);
        LocalDate now = LocalDate.now();

        assertThat(attendanceDao.isExistSameTime(now, attendance)).isTrue();
    }

    @Test
    @DisplayName("다른 날짜에 같은 멤버가 같은 시간에 있는지 조회한다.")
    void existSameTimeException() {
        insertAttendance();

        Attendance attendance = attendanceDao.findById(1L);
        LocalDate yesterday = LocalDate.now().minusDays(1);

        assertThat(attendanceDao.isExistSameTime(yesterday, attendance)).isFalse();
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
            CONFERENCE_TIME_SIX
        );

        List<Attendance> attendance = attendanceDao.findByDate(now);

        assertThat(attendance.contains(expectedAttendance)).isTrue();
    }

    @Test
    @DisplayName("날짜, 시간에 따른 출석부를 조회한다.")
    void findByTime() {
        insertAttendance();

        LocalDate now = LocalDate.now();
        Attendance expectedAttendance = new Attendance(
            1L,
            now,
            MEMBER_SALLY,
            CONFERENCE_TIME_SIX
        );

        List<Attendance> attendance = attendanceDao.findByDateTime(now, CONFERENCE_TIME_SIX);

        assertThat(attendance.contains(expectedAttendance)).isTrue();
    }

    @DisplayName("멤버, 시간대에 따른 출석부를 삭제한다.")
    @Test
    void delete() {
        Attendance attendance = insertAttendance();

        assertDoesNotThrow(() -> attendanceDao.delete(attendance));
    }

    @DisplayName("존재하지 않는 시간대에 따른 출석부를 삭제한다.")
    @Test
    void deleteExceptionInvalidTime() {
        Attendance fakeAttendance = new Attendance(
            MEMBER_SALLY,
            new ConferenceTime(3L, LocalTime.of(10, 0))
        );

        assertThatThrownBy(() ->
            attendanceDao.delete(fakeAttendance)
        ).isInstanceOf(NotFoundTimeException.class);
    }

    private Attendance insertAttendance() {
        Member madeMember = memberDao.insert(MEMBER_SALLY);
        ConferenceTime conferenceTime = conferenceTimeDao.findById(1L).orElseThrow(RuntimeException::new);
        Attendance attendance = new Attendance(LocalDate.now(), madeMember, conferenceTime);

        return attendanceDao.insert(attendance);
    }
}
