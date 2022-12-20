package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Surcharge;
import nextstep.subway.path.domain.policy.discount.AdultDiscount;
import nextstep.subway.path.domain.policy.discount.ChildDiscount;
import nextstep.subway.path.domain.policy.discount.DiscountPolicy;
import nextstep.subway.path.domain.policy.discount.YouthDiscount;

public class Fare {
    private static final String CAN_NOT_CHARGE_EXCEPTION = "6세 미만은 요금을 부과하지 않습니다.";
    private static final int DEFAULT_FARE = 1250;
    private static final int MIN_DISTANCE_STANDARD = 10;
    private static final int MAX_DISTANCE_STANDARD = 50;
    private static final int BETWEEN_TEN_AND_FIFTY_EXTRA_DISTANCE_PER = 5;
    private static final int OVER_FIFTY_EXTRA_DISTANCE_PER = 8;
    private static final int CHILD_MIN_AGE = 6;
    private static final int CHILD_MAX_AGE = 12;
    private static final int YOUTH_MIN_AGE = 13;
    private static final int YOUTH_MAX_AGE = 19;

    private Integer age;
    private Distance distance;
    private Surcharge surcharge;
    DiscountPolicy discountPolicy;

    public Fare(Distance distance, Surcharge surcharge, Integer age) {
        this.age = age;
        this.distance = distance;
        this.surcharge = surcharge;

        injectDiscountPolicy();
    }

    private void injectDiscountPolicy() {
        if (age == null) {
            discountPolicy = new AdultDiscount();
            return;
        }

        if (age < CHILD_MIN_AGE) {
            throw new IllegalArgumentException(CAN_NOT_CHARGE_EXCEPTION);
        }

        if (age >= CHILD_MIN_AGE && age <= CHILD_MAX_AGE) {
            discountPolicy = new ChildDiscount();
            return;
        }

        if (age >= YOUTH_MIN_AGE && age <= YOUTH_MAX_AGE) {
            discountPolicy = new YouthDiscount();
            return;
        }

        discountPolicy = new AdultDiscount();
    }

    private int adjustFarePolicyByDistance(int distance) {

        int fareByDistance = DEFAULT_FARE;

        if (distance > MIN_DISTANCE_STANDARD && distance < MAX_DISTANCE_STANDARD) {
            fareByDistance += calculateFareByDistance(distance - MIN_DISTANCE_STANDARD, BETWEEN_TEN_AND_FIFTY_EXTRA_DISTANCE_PER);
        }

        if (distance >= MAX_DISTANCE_STANDARD) {
            fareByDistance += calculateFareByDistance(MAX_DISTANCE_STANDARD - MIN_DISTANCE_STANDARD, BETWEEN_TEN_AND_FIFTY_EXTRA_DISTANCE_PER);
            fareByDistance += calculateFareByDistance(distance - MAX_DISTANCE_STANDARD, OVER_FIFTY_EXTRA_DISTANCE_PER);
        }

        return fareByDistance;
    }

    public int calculateFareByDistance(int distance, int extraDistancePer) {
        return (int) ((Math.ceil((distance - 1) / extraDistancePer) + 1) * 100);
    }

    public int value() {
        int fareByDistance = adjustFarePolicyByDistance(distance.value()) + surcharge.value();
        double discountFareByAge = discountPolicy.discount(fareByDistance);
        return (int) discountFareByAge;
    }
}
