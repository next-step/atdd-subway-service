package nextstep.subway.auth.domain;

import nextstep.subway.auth.application.AuthorizationException;

public class EmptyMember implements AuthMember {

    @Override
    public Long getId() {
        throw new AuthorizationException();
    }

    @Override
    public String getEmail() {
        throw new AuthorizationException();
    }

    @Override
    public Integer getAge() {
        throw new AuthorizationException();
    }
}
