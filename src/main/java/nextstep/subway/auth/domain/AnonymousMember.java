package nextstep.subway.auth.domain;

import nextstep.subway.fare.domain.FareCalculator;

public class AnonymousMember implements AuthMember{
    @Override
    public Long getId() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public Integer getAge() {
        return FareCalculator.MIN_ADULT_AGE;
    }

    @Override
    public boolean isLoginMember() {
        return false;
    }

}
