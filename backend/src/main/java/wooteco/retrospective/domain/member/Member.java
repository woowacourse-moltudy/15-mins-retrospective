package wooteco.retrospective.domain.member;

import java.util.Objects;

public class Member {

    private static final int MAX_LENGTH = 10;

    private final Long id;
    private final String name;

    public Member(String name) {
        this(null, name);
    }

    public Member(Long id, String name) {
        validateMember(name);
        this.id = id;
        this.name = name;
    }

    private void validateMember(String name) {
        validateName(name);
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.length() <= 0 || name.length() > MAX_LENGTH) {
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
        if (this == o) return true;
        if (!(o instanceof Member)) return false;

        Member member = (Member) o;

        return getId() != null ? getId().equals(member.getId()) : member.getId() == null;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }
}
