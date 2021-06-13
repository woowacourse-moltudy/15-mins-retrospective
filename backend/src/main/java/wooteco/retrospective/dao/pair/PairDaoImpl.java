package wooteco.retrospective.dao.pair;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wooteco.retrospective.domain.attendance.Time;
import wooteco.retrospective.domain.dao.PairDao;
import wooteco.retrospective.domain.member.Member;
import wooteco.retrospective.domain.pair.Pair;
import wooteco.retrospective.domain.pair.Pairs;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Repository
public class PairDaoImpl implements PairDao {

    public static final RowMapper<MemberWithGroupId> MEMBER_WITH_GROUP_ID_ROW_MAPPER = (rs, rn) -> {
        Long groupId = rs.getLong("group_id");
        Long id = rs.getLong("id");
        String name = rs.getString("name");

        return new MemberWithGroupId(groupId, new Member(id, name));
    };

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
        return pair.getMembers().stream().map(
                member -> new Object[]{pair.getGroupId(), member.getId()}
        ).collect(toList());
    }

    private List<Pair> membersToPairs(List<MemberWithGroupId> members) {
        Map<Long, List<MemberWithGroupId>> membersWithGroupId = members.stream()
                .collect(groupingBy(MemberWithGroupId::getGroupId));

        return membersWithGroupId.entrySet().stream()
                .map(this::toPair)
                .collect(toList());
    }

    private Pair toPair(Map.Entry<Long, List<MemberWithGroupId>> entry) {
        return new Pair(
                entry.getKey(), entry.getValue().stream()
                .map(MemberWithGroupId::getMember)
                .collect(toList())
        );
    }

    public Optional<Pairs> findByDateAndTime(LocalDateTime date, Time time) {
        List<MemberWithGroupId> members = jdbcTemplate.query(
                getFindByDateAndTimeSql(),
                MEMBER_WITH_GROUP_ID_ROW_MAPPER,
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                time.getTime()
        );

        if (members.isEmpty()) {
            return Optional.empty();
        }

        List<Pair> pairs = membersToPairs(members);

        return Optional.of(Pairs.from(pairs));
    }

    private String getFindByDateAndTimeSql() {
        return "SELECT P.group_id AS group_id, M.id AS id, M.name AS name " +
                "FROM PAIR P " +
                "INNER JOIN ATTENDANCE A " +
                "ON P.attendance_id = A.id " +
                "INNER JOIN MEMBER M " +
                "ON M.id = A.member_id " +
                "WHERE A.day=? and A.time_id = " +
                "(SELECT id FROM CONFERENCE_TIME WHERE time = ?);";
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
