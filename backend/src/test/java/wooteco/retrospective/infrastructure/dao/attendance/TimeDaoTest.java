package wooteco.retrospective.infrastructure.dao.attendance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.retrospective.domain.attendance.Time;

import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
class TimeDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TimeDao timeDao;

    @BeforeEach
    void setUp() {
        timeDao = new TimeDao(jdbcTemplate);
    }

    @DisplayName("모든 시간을 조회한다.")
    @Test
    void findAll() {
        List<Time> times = timeDao.findAll();

        Time time6 = new Time(1L, LocalTime.of(18, 0, 0));
        Time time10 = new Time(1L, LocalTime.of(22, 0, 0));

        assertThat(times).containsAll(Arrays.asList(time6, time10));
    }

    @DisplayName("저장된 시간을 변경한다.")
    @Test
    void update() {
        Time newTime = new Time(LocalTime.of(19, 0, 0));
        assertEquals(1, timeDao.update(1L, newTime));

        Time time = timeDao.findById(1L).orElseThrow(RuntimeException::new);
        assertThat(newTime).isEqualTo(time);
    }
}