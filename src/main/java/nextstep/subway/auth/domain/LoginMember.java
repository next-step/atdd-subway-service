package nextstep.subway.auth.domain;

import java.util.Objects;

public class LoginMember {
    private static final LoginMember GUEST_LOGIN_MEMBER = new LoginMember();

    private Long id;
    private String email;
    private Integer age;

    public LoginMember() {
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember empty() {
        return new LoginMember();
    }

    public boolean isNotDummy() {
        return !this.equals(GUEST_LOGIN_MEMBER);
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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        LoginMember that = (LoginMember)o;
        return Objects.equals(id, that.id) && Objects.equals(email, that.email)
            && Objects.equals(age, that.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, age);
    }

}
