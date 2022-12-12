package nextstep.subway.auth.domain;

import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;

public class LoginMember {
    private Long id;
    private Email email;
    private Age age;

    public LoginMember() {
    }

    public LoginMember(Long id, Email email, Age age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public Long id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public Age age() {
        return age;
    }
}
