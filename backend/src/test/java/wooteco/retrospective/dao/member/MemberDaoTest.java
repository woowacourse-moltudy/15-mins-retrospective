package wooteco.retrospective.dao.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.retrospective.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
    }

    @Test
    void insert() {
        Member member = new Member("피카", "123");
        Member insertMember = memberDao.insert(member);

        Member findById = memberDao.findById(insertMember.getId()).orElseThrow(IllegalArgumentException::new);

        assertThat(insertMember).isEqualTo(findById);
    }

    @Test
    void insertWithDuplicatedName() {
    }

    @Test
    void delete() {
        Member memberPika = new Member("피카", "123");
        Member memberLion = new Member("라이언", "123");

        Member insertMemberPika = memberDao.insert(memberPika);
        Member insertMemberLion = memberDao.insert(memberLion);

        memberDao.delete(insertMemberLion.getId());

        assertThat(memberDao.count()).isEqualTo(1);
        assertThat(memberDao.findAll()).containsExactly(insertMemberPika);
    }

    @Test
    void update() {
        Member memberPika = new Member("피카", "123");
        Member insertMemberPika = memberDao.insert(memberPika);
        Member memberLion = new Member("라이언", "456");

        memberDao.update(insertMemberPika.getId(), memberLion);

        Member updateMember = memberDao.findById(insertMemberPika.getId()).orElseThrow(IllegalArgumentException::new);

        assertThat(updateMember.getName()).isEqualTo(memberLion.getName());
        assertThat(updateMember.getPassword()).isEqualTo(memberLion.getPassword());
    }

}