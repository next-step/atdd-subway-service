package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;

import static nextstep.subway.line.domain.Distance.DISTANCE_UNIT_LEVEL1;
import static nextstep.subway.line.domain.Distance.DISTANCE_UNIT_LEVEL2;
import static nextstep.subway.path.domain.Fare.*;

enum DiscountPolicy {
    LEVEL1(ADDITIONAL_FARE_DISTANCE_LEVEL1, DISTANCE_UNIT_LEVEL1),
    LEVEL2(ADDITIONAL_FARE_DISTANCE_LEVEL2, DISTANCE_UNIT_LEVEL2),
    DEFAULT(null, null);

    private final Integer distance;
    private final Integer distanceUnit;

    DiscountPolicy(Integer distance, Integer distanceUnit) {
        this.distance = distance;
        this.distanceUnit = distanceUnit;
    }

    public static int calculateAdditionalFareOfDistance(final Distance distance) {
        DiscountPolicy discountPolicy = Arrays.stream(DiscountPolicy.values())
                .filter(policy -> policy != DiscountPolicy.DEFAULT && distance.isBiggerThen(policy.distance))
                .findFirst()
                .orElse(DiscountPolicy.DEFAULT);

        if (discountPolicy == DiscountPolicy.DEFAULT) {
            return 0;
        }

        return calculateOverFare(distance.minus(discountPolicy.distance), discountPolicy.distanceUnit);
    }

    private static int calculateOverFare(double distance, int distanceUnit) {
        return (int) ((Math.floor((distance - 1) / distanceUnit) + 1) * ADDITIONAL_FARE_UNIT);
    }
}
