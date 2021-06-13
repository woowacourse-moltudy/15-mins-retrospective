package wooteco.retrospective.dao.pair;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.dao.attendance.AttendanceDao;
import wooteco.retrospective.dao.member.MemberDao;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.Pairs;
import wooteco.retrospective.domain.pair.matchpolicy.DefaultMatchPolicy;
import wooteco.retrospective.domain.pair.matchpolicy.MatchPolicy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;

@Repository
public class PairDao {

    private final AttendanceDao attendanceDao;
    private final MemberDao memberDao;
    private final JdbcTemplate jdbcTemplate;


    public PairDao(AttendanceDao attendanceDao, MemberDao memberDao, JdbcTemplate jdbcTemplate) {
        this.attendanceDao = attendanceDao;
        this.memberDao = memberDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Transactional
    public Pairs insert(Pairs pairs) {
        String sql = "INSERT INTO PAIR (group_id, attendance_id) VALUES (?, ?)";

        pairs.getPairs().forEach(
                pair -> {
                    jdbcTemplate.batchUpdate(sql, getValuesForEachPair(pair));
                }
        );

        return pairs;
    }

    private List<Object[]> getValuesForEachPair(Pair pair) {
        return pair.getMembers().stream().map(
                member -> new Object[]{pair.getGroupId(), member.getId()}
        ).collect(toList());
    }

    private List<Pair> membersToPairs(List<MemberWithGroupId> members) {
        Map<Long, List<MemberWithGroupId>> membersWithGroupId = members.stream()
                .collect(groupingBy(MemberWithGroupId::getGroupId));

        return membersWithGroupId.entrySet().stream().map(
                entry -> new Pair(
                        entry.getKey(), entry.getValue().stream()
                        .map(MemberWithGroupId::getMember)
                        .collect(toList())
                )
        ).collect(toList());
    }

    public Pairs findByDateAndTime(LocalDateTime date, Time time) {
        final String sql = "SELECT P.group_id AS group_id, M.id AS id, M.name AS name " +
                "FROM PAIR P " +
                "INNER JOIN ATTENDANCE A " +
                "ON P.attendance_id = A.id " +
                "INNER JOIN MEMBER M " +
                "ON M.id = A.member_id " +
                "WHERE A.day=? and A.time_id = " +
                "(SELECT id FROM CONFERENCE_TIME WHERE time = ?);";

        List<MemberWithGroupId> members = jdbcTemplate.query(sql, (rs, rn) -> {
            Long groupId = rs.getLong("group_id");
            Long id = rs.getLong("id");
            String name = rs.getString("name");

            return new MemberWithGroupId(groupId, new Member(id, name));
        }, date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), time.getTime());

        List<Pair> pairs = membersToPairs(members);

        return Pairs.from(pairs);
    }

    private static class MemberWithGroupId {

        private final Long groupId;
        private final Member member;

        public MemberWithGroupId(Long groupId, Member member) {
            this.groupId = groupId;
            this.member = member;
        }

        public Long getGroupId() {
            return groupId;
        }

        public Member getMember() {
            return member;
        }
    }
}
