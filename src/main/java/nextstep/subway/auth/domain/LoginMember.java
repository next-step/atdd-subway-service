package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;

public class LoginMember {

    private static final LoginMember GUEST = new LoginMember();
    private Long id;
    private String email;
    private Integer age;

    private LoginMember() {
    }

    private LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember of(Long id, String email, Integer age) {
        return new LoginMember(id, email, age);
    }

    public static LoginMember fromGuest() {
        return GUEST;
    }

    public Long getId() {
        if (isGuest()) {
            throw new AuthorizationException("비로그인 상태입니다.");
        }
        return id;
    }

    public boolean isGuest() {
        return this == GUEST;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    @Override
    public String toString() {
        return "LoginMember{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
