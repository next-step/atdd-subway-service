package nextstep.subway.auth.domain;

import nextstep.subway.exception.AuthorizationException;

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

    public void validation() {
        if (id == null) {
            throw new AuthorizationException("로그인된 멤버가 없습니다.");
        }
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
}
