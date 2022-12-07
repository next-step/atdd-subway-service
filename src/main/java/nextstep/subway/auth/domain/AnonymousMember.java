package nextstep.subway.auth.domain;

import nextstep.subway.fare.domain.SubwayFareCalculator;

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
        return SubwayFareCalculator.MIN_ADULT_AGE;
    }

    @Override
    public boolean isLoginMember() {
        return false;
    }

}
