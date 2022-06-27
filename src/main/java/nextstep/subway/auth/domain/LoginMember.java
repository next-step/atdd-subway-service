package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.Email;

import java.util.Objects;

public class LoginMember {
    private final Long id;
    private final Email email;
    private final Age age;

    private LoginMember(final Long id, final String email, final Integer age) {
        this.id = id;
        this.email = Email.of(email);
        this.age = Age.of(age);
    }

    public static LoginMember of(final Long id, final String email, final Integer age) {
        return new LoginMember(id, email, age);
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email.getValue();
    }

    public Integer getAge() {
        return age.getValue();
    }

    @Override
    public String toString() {
        return "LoginMember{" +
                "id=" + id +
                ", email=" + email +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final LoginMember that = (LoginMember) o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email) && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, age);
    }
}
