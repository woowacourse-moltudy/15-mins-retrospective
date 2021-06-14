package wooteco.retrospective.infrastructure.dao.pair;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.domain.attendance.Attendance;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.dao.PairDao;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.Pairs;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
public class PairDaoImpl implements PairDao {

    private final JdbcTemplate jdbcTemplate;

    public PairDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Pairs insert(Pairs pairs) {
        String sql = "INSERT INTO PAIR (group_id, attendance_id) VALUES (?, ?)";

        pairs.getPairs().forEach(
                pair -> jdbcTemplate.batchUpdate(sql, getValuesForEachPair(pair))
        );

        return pairs;
    }

    private List<Object[]> getValuesForEachPair(Pair pair) {
        return pair.getAttendances().stream().map(
                attendance -> new Object[]{pair.getGroupId(), attendance.getId()}
        ).collect(toList());
    }

    private List<Pair> membersToPairs(List<AttendanceWithGroupId> members) {
        Map<Long, List<AttendanceWithGroupId>> membersWithGroupId = members.stream()
                .collect(groupingBy(AttendanceWithGroupId::getGroupId));

        return membersWithGroupId.entrySet().stream()
                .map(this::toPair)
                .collect(toList());
    }

    private Pair toPair(Map.Entry<Long, List<AttendanceWithGroupId>> entry) {
        return new Pair(
                entry.getKey(), entry.getValue().stream()
                .map(AttendanceWithGroupId::getAttendance)
                .collect(toList())
        );
    }

    public Optional<Pairs> findByDateAndTime(LocalDate date, Time time) {
        List<AttendanceWithGroupId> members = jdbcTemplate.query(
                getFindByDateAndTimeSql(),
                rowMapperForFindByDateAndTime(time),
                date,
                time.getTime()
        );

        if (members.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(Pairs.from(membersToPairs(members)));
    }

    private RowMapper<AttendanceWithGroupId> rowMapperForFindByDateAndTime(Time time) {
        return (rs, rn) -> {
            Long groupId = rs.getLong("group_id");
            Attendance attendance = createAttendance(time, rs);

            return new AttendanceWithGroupId(groupId, attendance);
        };
    }

    private Attendance createAttendance(Time time, java.sql.ResultSet rs) throws SQLException {
        Long attendanceId = rs.getLong("attendance_id");
        LocalDate date = rs.getObject("date", LocalDate.class);

        return new Attendance(attendanceId, date, createMember(rs), time);
    }

    private Member createMember(java.sql.ResultSet rs) throws SQLException {
        String name = rs.getString("name");
        Long memberId = rs.getLong("member_id");

        return new Member(memberId, name);
    }

    private String getFindByDateAndTimeSql() {
        return "SELECT " +

                "P.group_id AS group_id, " +
                "M.id AS member_id, " +
                "M.name AS name, " +
                "A.id AS attendance_id, " +
                "A.date AS date " +

                "FROM PAIR P " +
                "INNER JOIN ATTENDANCE A " +
                "ON P.attendance_id = A.id " +
                "INNER JOIN MEMBER M " +
                "ON M.id = A.member_id " +
                "WHERE A.date=? and A.time_id = " +
                "(SELECT id FROM CONFERENCE_TIME WHERE time = ?)";
    }

    private static class AttendanceWithGroupId {

        private final Long groupId;
        private final Attendance attendance;

        public AttendanceWithGroupId(Long groupId, Attendance attendance) {
            this.groupId = groupId;
            this.attendance = attendance;
        }

        public Long getGroupId() {
            return groupId;
        }

        public Attendance getAttendance() {
            return attendance;
        }
    }
}
