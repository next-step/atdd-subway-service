package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;

public class LoginMember {
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

    public Long getId() {
        if (id == null) {
            throw new AuthorizationException("유효하지 않은 토큰입니다.");
        }
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public boolean loggedIn() {
        return id != null;
    }
}
