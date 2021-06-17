package wooteco.retrospective.infrastructure.dao.attendance;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.ConferenceTime;
import wooteco.retrospective.exception.NotFoundMemberException;
import wooteco.retrospective.exception.NotFoundTimeException;
import wooteco.retrospective.infrastructure.dao.member.MemberDao;

@Repository
@Transactional
public class AttendanceDao {

    private final JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private ConferenceTimeDao conferenceTimeDao;

    private final RowMapper<Attendance> rowMapper = (resultSet, rowNumber) ->
        new Attendance(
            resultSet.getLong("id"),
            resultSet.getObject("date", LocalDate.class),
            memberDao.findById(resultSet.getLong("member_id")).orElseThrow(NotFoundMemberException::new),
            conferenceTimeDao.findById(resultSet.getLong("conference_time_id")).orElseThrow(NotFoundTimeException::new)
        );

    public AttendanceDao(JdbcTemplate jdbcTemplate, MemberDao memberDao,
        ConferenceTimeDao conferenceTimeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.memberDao = memberDao;
        this.conferenceTimeDao = conferenceTimeDao;
    }

    public Attendance insert(Attendance attendance) {
        String query = "INSERT INTO ATTENDANCE(date, member_id, conference_time_id) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(query, new String[] {"id"});
            ps.setDate(1, Date.valueOf(attendance.getDate()));
            ps.setLong(2, attendance.getMemberId());
            ps.setLong(3, attendance.getConferenceTimeId());

            return ps;
        }, keyHolder);

        return new Attendance(
            Objects.requireNonNull(keyHolder.getKey()).longValue(),
            attendance.getDate(),
            attendance.getMember(),
            attendance.getConferenceTime()
        );
    }

    public Attendance findById(long id) {   //TODO: 예외처리
        String query = "SELECT * FROM ATTENDANCE WHERE id = ?";

        return jdbcTemplate.queryForObject(query, rowMapper, id);
    }

    public boolean isExistSameTime(LocalDate date, Attendance attendance) {
        String query = "SELECT EXISTS (SELECT * FROM ATTENDANCE WHERE date = ? AND member_id = ? AND conference_time_id = ?)";

        return jdbcTemplate.queryForObject(query, boolean.class, date, attendance.getMemberId(), attendance.getConferenceTimeId());
    }

    public List<Attendance> findByDate(LocalDate date) {
        String query = "SELECT * FROM ATTENDANCE WHERE date = ?";

        return jdbcTemplate.query(query, rowMapper, date);
    }

    public List<Attendance> findByDateTime(LocalDate date, ConferenceTime conferenceTime) {
        String query = "SELECT * FROM ATTENDANCE WHERE date = ? AND conference_time_id = ?";

        return jdbcTemplate.query(query, rowMapper, date, conferenceTime.getId());
    }

    public void delete(Attendance attendance) {
        String query = "DELETE FROM ATTENDANCE WHERE member_id = ? AND conference_time_id = ?";

        if (jdbcTemplate.update(query, attendance.getMemberId(), attendance.getConferenceTimeId()) != 1) {
            throw new NotFoundTimeException();
        }
    }
}
