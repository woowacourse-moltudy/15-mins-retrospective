package wooteco.retrospective.domain.member;

import java.util.Objects;

public class Member {

    private static final int MAX_LENGTH = 10;

    private final Long id;
    private final String name;
    private final String password;

    public Member(String name, String password) {
        this(null, name, password);
    }

    public Member(Long id, String name, String password) {
        validateMember(name, password);
        this.id = id;
        this.name = name;
        this.password = password;
    }

    private void validateMember(String name, String password) {
        validateName(name);
        validatePassword(password);
    }

    private void validateName(String name) {
        if (Objects.isNull(name) || name.length() <= 0 || name.length() > MAX_LENGTH) {
            throw new IllegalArgumentException();
        }
    }

    private void validatePassword(String password) {
        if (Objects.isNull(password) || password.length() <= 0 || password.length() > MAX_LENGTH) {
            throw new IllegalArgumentException();
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
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
