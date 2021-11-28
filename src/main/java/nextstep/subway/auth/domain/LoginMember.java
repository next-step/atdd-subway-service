package nextstep.subway.auth.domain;

import io.jsonwebtoken.lang.Assert;
import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;

public final class LoginMember {

    private final Long id;
    private final Email email;
    private final Age age;

    private LoginMember(long id, Email email, Age age) {
        Assert.notNull(email, "이메일은 필수입니다.");
        Assert.notNull(age, "나이는 필수입니다.");
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember of(long id, Email email, Age age) {
        return new LoginMember(id, email, age);
    }

    public long id() {
        return id;
    }

    public Email email() {
        return email;
    }

    public Age age() {
        return age;
    }
}
