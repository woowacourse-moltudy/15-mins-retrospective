package wooteco.retrospective.domain.member;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class Member {

    private static final int MAX_LENGTH = 10;

    private Long id;
    private String name;


    protected Member() {
    }

    public Member(String name) {
        this(null, name);
    }

    public Member(Long id, String name) {
        validateMember(name);
        this.id = id;
        this.name = name;
    }

    private void validateMember(String name) {
        if (Objects.isNull(name)) {
            throw new IllegalArgumentException();
        }

        if (name.length() <= 0 || name.length() > MAX_LENGTH ||
            StringUtils.containsWhitespace(name)) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }

        Member member = (Member) o;

        return getId() != null ? getId().equals(member.getId()) : member.getId() == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
