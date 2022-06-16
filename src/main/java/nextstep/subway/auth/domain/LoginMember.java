package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;
import nextstep.subway.line.domain.Age;

public class LoginMember {
    private static final int DEFAULT_AGE = 19;

    private Long id;
    private String email;
    private Age age;

    public LoginMember() {
        this.age = Age.of(DEFAULT_AGE);
    }

    public LoginMember(Long id, String email, Integer age) {
        this.id = id;
        this.email = email;
        this.age = Age.of(age);
    }

    public Long getId() {
        if (null == id) {
            throw new AuthorizationException();
        }
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Age getAge() {
        return age;
    }
}
