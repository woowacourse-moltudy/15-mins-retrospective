package wooteco.retrospective.domain.attendance.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import wooteco.retrospective.common.Fixture;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.conference_time.ConferenceTime;
import wooteco.retrospective.domain.conference_time.repository.ConferenceTimeRepository;
import wooteco.retrospective.domain.member.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static wooteco.retrospective.common.Fixture.*;

@DataJpaTest
class AttendanceRepositoryTest {

    @Autowired
    private ConferenceTimeRepository conferenceTimeRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Attendance 손너잘1;
    private Attendance 손너잘2;

    @BeforeEach
    void setUp() {
        attendanceRepository.deleteAll();

        ConferenceTime timeSix = conferenceTimeRepository.save(new ConferenceTime(TIME_SIX));
        손너잘1 = new Attendance(TODAY, new Member("손너잘1"), timeSix);
        손너잘2 = new Attendance(YESTERDAY, new Member("손너잘2"), timeSix);

        attendanceRepository.save(손너잘1);
        attendanceRepository.save(손너잘2);

        testEntityManager.flush();
        testEntityManager.clear();
    }

    @Test
    void findAttendanceByDateAndMember() {
        Optional<Attendance> actual = attendanceRepository.findAttendanceByDateAndMember(TODAY, 손너잘1.getMember());
        assertThat(actual).isPresent();
    }

    @Test
    void findAttendanceByDateAndConferenceTime() {
        List<Attendance> actual = attendanceRepository.findAttendanceByDateAndConferenceTime(TODAY, 손너잘1.getConferenceTime());
        assertThat(actual).hasSize(1);
    }
}
