package wooteco.retrospective.domain.member;

public class Member {

    private final Long id;
    private final String name;
    private final String password;

    public Member(String name, String password) {
        this(null, name, password);
    }

    public Member(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
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
