package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.common.FareCalculateUtils;

public class AgeOfTeenagerPolicy implements DiscountOfAgeCalculator {

    private static final int BASE_FARE = 350;
    private static final double DISCOUNT_PERCENT = 0.2;
    private static final int MIN_AGE = 13;
    private static final int MAX_AGE = 19;

    @Override
    public boolean isTarget(int age) {
        if (age >= MIN_AGE && age < MAX_AGE) {
            return true;
        }
        return false;
    }

    @Override
    public int discount(int totalFare) {
        return FareCalculateUtils.getFareAfterDiscount(totalFare, BASE_FARE, DISCOUNT_PERCENT);
    }
}
