package nextstep.subway.path.domain;

import nextstep.subway.path.policy.AgeDiscountPolicy;

import static nextstep.subway.exception.ErrorMessage.FARE_CANNOT_BE_ZERO;

public class Fare {

    private int fare;
    public static final int BASIC_FARE = 1250;
    public static final int DEDUCTION_FARE = 350;
    private static final int FARE_ZERO = 0;

    private Fare() {

    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public static Fare basicFare() {
        return new Fare(BASIC_FARE);
    }


    public Fare plus(int extraFare) {
        fare += extraFare;
        validateFare(fare);
        return this;
    }

    public Fare ageDiscount(AgeDiscountPolicy discountPolicy) {
        fare = discountPolicy.discount(fare);
        validateFare(fare);
        return this;
    }

    private void validateFare(int fare) {
        if (fare <= FARE_ZERO) {
            throw new IllegalArgumentException(FARE_CANNOT_BE_ZERO.getMessage());
        }
    }

    public int getFare() {
        return fare;
    }
}
