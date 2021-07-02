package nextstep.subway.path.additionalfarepolicy.memberfarepolicy;

import nextstep.subway.path.domain.Fare;

public class TeenagersDiscountPolicy implements MemberDiscountPolicy {
    public static final int MIN_AGE = 13;
    public static final int MAX_AGE = 19;
    private static final double DISCOUNT_PER = 0.2d;
    private static final Fare DEFAULT_DISCOUNT_FARE = new Fare(350);

    @Override
    public Fare applyDiscount(Fare fare) {
        return fare.sub(DEFAULT_DISCOUNT_FARE).sub(discount(fare));
    }

    @Override
    public boolean isAvailable(int age) {
        return MIN_AGE <= age && age < MAX_AGE;
    }

    private Fare discount(Fare fare) {
        return fare.sub(DEFAULT_DISCOUNT_FARE).discountFare(DISCOUNT_PER);
    }


}
