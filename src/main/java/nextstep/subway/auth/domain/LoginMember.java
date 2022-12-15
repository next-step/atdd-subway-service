package nextstep.subway.auth.domain;

import nextstep.subway.common.domain.Age;
import nextstep.subway.common.domain.Email;

public class LoginMember {

    private Long id;
    private Email email;
    private Age age;

    static class GuestMember extends LoginMember {
        public GuestMember() {
            super(null, Email.from("guest@email.com"), Age.from(0));
        }

        @Override
        public boolean isGuest() {
            return true;
        }
    }

    private LoginMember(Long id, Email email, Age age) {
        this.id = id;
        this.email = email;
        this.age = age;
    }

    public static LoginMember of(Long id, Email email, Age age) {
        return new LoginMember(id, email, age);
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

    public boolean isGuest() {
        return false;
    }

    public static LoginMember guest() {
        return new LoginMember.GuestMember();
    }
}
