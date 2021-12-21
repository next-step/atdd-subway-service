package nextstep.subway.domain.auth.domain;

import nextstep.subway.domain.auth.exception.AnonymousUserException;

public class AnonymousUser extends User {

    @Override
    public boolean isChildren() {
        throw new AnonymousUserException();
    }

    @Override
    public boolean isTeenager() {
        throw new AnonymousUserException();
    }

    @Override
    public Long getId() {
        throw new AnonymousUserException();
    }

    @Override
    public String getEmail() {
        throw new AnonymousUserException();
    }

    @Override
    public Integer getAge() {
        throw new AnonymousUserException();
    }

    @Override
    public boolean isLoginUser() {
        return false;
    }
}
