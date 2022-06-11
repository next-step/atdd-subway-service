package nextstep.subway.auth.domain;

import nextstep.subway.policy.DiscountPolicy;
import nextstep.subway.policy.NonDiscountPolicy;

public class GuestMember implements ServiceMember{
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
        return null;
    }

    @Override
    public DiscountPolicy getDiscountPolicy() {
        return new NonDiscountPolicy();
    }
}
