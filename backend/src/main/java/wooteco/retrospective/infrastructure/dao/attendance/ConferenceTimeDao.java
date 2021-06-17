package wooteco.retrospective.infrastructure.dao.attendance;

import java.sql.PreparedStatement;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.domain.attendance.ConferenceTime;

@Repository
@Transactional
public class ConferenceTimeDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<ConferenceTime> rowMapper = (resultSet, rowNumber) ->
        new ConferenceTime(
            resultSet.getLong("id"),
            resultSet.getObject("conference_time", LocalTime.class)
        );

    public ConferenceTimeDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<ConferenceTime> findAll() {
        String query = "SELECT * FROM CONFERENCE_TIME";

        return jdbcTemplate.query(query, rowMapper);
    }

    public int update(Long conferenceTimeId, ConferenceTime conferenceTime) {
        String query = "UPDATE CONFERENCE_TIME SET conference_time = ? WHERE id = ?";

        return jdbcTemplate.update(query, conferenceTime.getConferenceTime(), conferenceTimeId);
    }

    public Optional<ConferenceTime> findById(long id) {
        String query = "SELECT * FROM CONFERENCE_TIME WHERE id = ?";

        return Optional.ofNullable(jdbcTemplate.queryForObject(query, rowMapper, id));
    }

    public ConferenceTime insert(ConferenceTime conferenceTime) {
        String query = "INSERT INTO CONFERENCE_TIME(conference_time) values(?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, new String[] {"id"});
            ps.setTime(1, java.sql.Time.valueOf(conferenceTime.getConferenceTime()));

            return ps;
        }, keyHolder);

        return new ConferenceTime(
            Objects.requireNonNull(keyHolder.getKey()).longValue(),
            conferenceTime.getConferenceTime()
        );
    }
}
