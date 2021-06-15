package wooteco.retrospective.infrastructure.dao.attendance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;

import wooteco.retrospective.domain.attendance.ConferenceTime;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
class ConferenceTimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ConferenceTimeDao conferenceTimeDao;

    @BeforeEach
    void setUp() {
        conferenceTimeDao = new ConferenceTimeDao(jdbcTemplate);
    }

    @DisplayName("모든 시간을 조회한다.")
    @Test
    void findAll() {
        List<ConferenceTime> conferenceTimes = conferenceTimeDao.findAll();

        ConferenceTime conferenceTime6 = new ConferenceTime(1L, LocalTime.of(18, 0, 0));
        ConferenceTime conferenceTime10 = new ConferenceTime(1L, LocalTime.of(22, 0, 0));

        assertThat(conferenceTimes).containsAll(Arrays.asList(conferenceTime6, conferenceTime10));
    }

    @DisplayName("저장된 시간을 변경한다.")
    @Test
    void update() {
        ConferenceTime newConferenceTime = new ConferenceTime(LocalTime.of(19, 0, 0));
        assertEquals(1, conferenceTimeDao.update(1L, newConferenceTime));

        ConferenceTime conferenceTime = conferenceTimeDao.findById(1L).orElseThrow(RuntimeException::new);
        assertThat(newConferenceTime).isEqualTo(conferenceTime);
    }
}