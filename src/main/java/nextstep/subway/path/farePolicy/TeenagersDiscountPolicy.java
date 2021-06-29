package nextstep.subway.path.farePolicy;

import nextstep.subway.line.domain.Fare;

public class TeenagersDiscountPolicy implements MemberDiscountPolicyService{
    public static final int MIN_AGE = 13;
    public static final int MAX_AGE = 19;
    private static final double DISCOUNT_PER = 0.2d;
    private static final int DEFAULT_DISCOUNT_FARE = 350;

    @Override
    public Fare applyDiscount(Fare fare) {
        return new Fare(fare.amount() - DEFAULT_DISCOUNT_FARE - discount(fare));
    }

    private int discount(Fare fare) {
        return (int) Math.ceil(((fare.amount() - DEFAULT_DISCOUNT_FARE) * DISCOUNT_PER));
    }
}
