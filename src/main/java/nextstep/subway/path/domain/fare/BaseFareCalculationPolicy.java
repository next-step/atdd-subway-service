package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.discount.DiscountPolicy;

public class BaseFareCalculationPolicy implements FareCalculationPolicy {
    private final DiscountPolicy discountPolicy;

    public BaseFareCalculationPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    @Override
    public int calculateFare() {
        return discountPolicy.discount(DEFAULT_FARE);
    }
}
