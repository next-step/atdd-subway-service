package nextstep.subway.path.fare.policy.discount;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.fare.Fare;

public enum DiscountFarePolicyType {
    AGE(new AgeDiscountFarePolicyStrategy()),
    ;

    private final DiscountFarePolicyStrategy discountFarePolicyStrategy;

    DiscountFarePolicyType(DiscountFarePolicyStrategy discountFarePolicyStrategy) {
        this.discountFarePolicyStrategy = discountFarePolicyStrategy;
    }

    public Fare discount(LoginMember loginMember, Fare fare) {
        return discountFarePolicyStrategy.discount(loginMember, fare);
    }
}
