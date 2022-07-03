package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.discount.DiscountPolicy;

public class DistanceFareCalculationPolicy implements FareCalculationPolicy {
    private static final int DEFAULT_FARE_MAX_DISTANCE = 10;
    private static final int FIRST_ADDITIONAL_FARE_MAX_DISTANCE = 50;
    private static final int FIRST_ADDITIONAL_FARE_DISTANCE_UNIT = 5;
    private static final int SECOND_ADDITIONAL_FARE_DISTANCE_UNIT = 8;
    private static final int ADDITIONAL_FARE_UNIT = 100;

    private final DiscountPolicy discountPolicy;
    private final int firstAdditionalFareDistance;
    private final int secondAdditionalFareDistance;

    public DistanceFareCalculationPolicy(DiscountPolicy discountPolicy, int distance) {
        this.discountPolicy = discountPolicy;
        this.firstAdditionalFareDistance = calculateFirstAdditionalFareDistance(distance);
        this.secondAdditionalFareDistance = calculateSecondAdditionalFareDistance(distance);
    }

    private int calculateFirstAdditionalFareDistance(int distance) {
        if (distance <= DEFAULT_FARE_MAX_DISTANCE) {
            return 0;
        }
        return Math.min(distance - DEFAULT_FARE_MAX_DISTANCE,
                FIRST_ADDITIONAL_FARE_MAX_DISTANCE - DEFAULT_FARE_MAX_DISTANCE);
    }

    private int calculateSecondAdditionalFareDistance(int distance) {
        if (distance <= FIRST_ADDITIONAL_FARE_MAX_DISTANCE) {
            return 0;
        }
        return distance - FIRST_ADDITIONAL_FARE_MAX_DISTANCE;
    }

    @Override
    public int calculateFare() {
        int additionalFare = calculateFirstAdditionalFare() + calculateSecondAdditionalFare();
        return discountPolicy.discount(DEFAULT_FARE + additionalFare);
    }

    private int calculateFirstAdditionalFare() {
        return (int) Math.ceil(firstAdditionalFareDistance / FIRST_ADDITIONAL_FARE_DISTANCE_UNIT)
                * ADDITIONAL_FARE_UNIT;
    }

    private int calculateSecondAdditionalFare() {
        return (int) Math.ceil(secondAdditionalFareDistance / SECOND_ADDITIONAL_FARE_DISTANCE_UNIT)
                * ADDITIONAL_FARE_UNIT;
    }
}
