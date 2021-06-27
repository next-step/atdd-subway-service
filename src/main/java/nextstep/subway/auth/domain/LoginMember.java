package nextstep.subway.auth.domain;

import java.util.Objects;

public class LoginMember {
    public static final LoginMember ANONYMOUS = new LoginMember(-1L ,null ,null);

    private final Long id;
    private final String email;
    private final Integer age;

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public boolean isAnonymous() {
        return this.equals(ANONYMOUS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoginMember that = (LoginMember)o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
