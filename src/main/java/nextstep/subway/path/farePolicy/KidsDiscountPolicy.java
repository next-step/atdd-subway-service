package nextstep.subway.path.farePolicy;

import nextstep.subway.line.domain.Fare;

public class KidsDiscountPolicy implements MemberDiscountPolicyService{
    public static final int MIN_AGE = 6;
    public static final int MAX_AGE = 13;
    private static final double DISCOUNT_PER = 0.5d;
    private static final int DEFAULT_DISCOUNT_FARE = 350;

    @Override
    public Fare applyDiscount(Fare fare) {
        return new Fare(fare.amount() - DEFAULT_DISCOUNT_FARE - discount(fare));
    }

    private int discount(Fare fare) {
        return (int) Math.ceil(((fare.amount() - DEFAULT_DISCOUNT_FARE) * DISCOUNT_PER));
    }
}
