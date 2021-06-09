package wooteco.retrospective.dao.member;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.retrospective.domain.member.Member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@JdbcTest
class MemberDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private MemberDao memberDao;
    private Member memberPika;
    private Member memberLion;

    @BeforeEach
    void setUp() {
        memberDao = new MemberDao(jdbcTemplate);
        memberPika = new Member("피카");
        memberLion = new Member("라이언");
    }

    @DisplayName("Member 추가")
    @Test
    void insert() {
        Member insertMember = memberDao.insert(memberPika);

        Member findById = memberDao.findById(insertMember.getId()).orElseThrow(IllegalArgumentException::new);

        assertThat(insertMember).isEqualTo(findById);
    }

    @DisplayName("중복된 Member를 추가할 시 에러")
    @Test
    void insertWithDuplicatedName() {
        Member duplicatedMember = new Member("피카");

        memberDao.insert(memberPika);

        assertThatThrownBy(() -> memberDao.insert(duplicatedMember))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("Member 삭제")
    @Test
    void delete() {
        Member insertMemberPika = memberDao.insert(memberPika);
        Member insertMemberLion = memberDao.insert(memberLion);

        memberDao.delete(insertMemberLion.getId());

        assertThat(memberDao.count()).isEqualTo(1);
        assertThat(memberDao.findAll()).containsExactly(insertMemberPika);
    }

    @DisplayName("Member 수정")
    @Test
    void update() {
        Member insertMemberPika = memberDao.insert(memberPika);

        memberDao.update(insertMemberPika.getId(), memberLion);

        Member updateMember = memberDao.findById(insertMemberPika.getId()).orElseThrow(IllegalArgumentException::new);

        assertThat(updateMember.getName()).isEqualTo(memberLion.getName());
    }

}