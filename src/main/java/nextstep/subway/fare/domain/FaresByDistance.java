package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static java.lang.Integer.MAX_VALUE;

public enum FaresByDistance {

    LONG(new Distance(51), 8, (Distance distance) -> distance.minus(new Distance(50))),
    SHORT(new Distance(11), 5, (Distance distance) -> {
        if(distance.isLessThan(new Distance(50))) {
            return distance.minus(new Distance(10));
        }
        return new Distance(40);
    });

    public static final Fare ADDITIONAL_FARE_UNIT = new Fare(100);

    private final Distance moreDistance;
    private final int dividingValue;
    private final UnaryOperator<Distance> applyingStandard;

    FaresByDistance(Distance moreDistance, int dividingValue, UnaryOperator<Distance> applyingDistanceFunction) {
        this.moreDistance = moreDistance;
        this.applyingStandard = applyingDistanceFunction;
        this.dividingValue = dividingValue;
    }

    public static Fare calculate(Fare base, Distance distance) {

        List<FaresByDistance> faresByDistances = findFaresByDistance(distance);

        for(FaresByDistance faresByDistance : faresByDistances) {
            Distance dividedDistance = faresByDistance.applyingStandard.apply(distance);
            int additionalFareDistance = dividedDistance.getDividedValue(faresByDistance.dividingValue);

            base = base.add(ADDITIONAL_FARE_UNIT.multiply(new Rate(additionalFareDistance)));
        }

        return base;
    }

    private static List<FaresByDistance> findFaresByDistance(Distance distance) {
        return Arrays.stream(values())
                .filter(value -> distance.isMoreThan(value.moreDistance))
                .collect(Collectors.toList());
    }
}
