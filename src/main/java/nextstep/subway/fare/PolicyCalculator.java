package nextstep.subway.fare;

import nextstep.subway.fare.policy.discount.DiscountPolicy;

public class PolicyCalculator {
    private final DiscountPolicy discountPolicy;

    public PolicyCalculator(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    public int calculate(int fare, int extraFare) {
        return discountPolicy.calculateDiscountAmount(fare, extraFare);
    }
}
