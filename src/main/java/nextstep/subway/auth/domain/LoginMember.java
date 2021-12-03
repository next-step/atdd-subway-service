package nextstep.subway.auth.domain;

import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;
import nextstep.subway.common.exception.AuthorizationException;
import org.springframework.util.Assert;

public final class LoginMember {

    private static final LoginMember GUEST_MEMBER = new LoginMember();

    private Long id;
    private Email email;
    private Age age;

    private LoginMember() {
    }

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

    public static LoginMember guest() {
        return GUEST_MEMBER;
    }

    public long id() {
        validateGuest();
        return id;
    }

    public boolean isChild() {
        if (isGuest()) {
            return false;
        }
        return age.isChild();
    }

    public boolean isYouth() {
        if (isGuest()) {
            return false;
        }
        return age.isYouth();
    }

    private void validateGuest() {
        if (isGuest()) {
            throw new AuthorizationException("로그인이 필요합니다.");
        }
    }

    private boolean isGuest() {
        return this == GUEST_MEMBER;
    }

    @Override
    public String toString() {
        return "LoginMember{" +
            "id=" + id +
            ", email=" + email +
            ", age=" + age +
            '}';
    }
}
