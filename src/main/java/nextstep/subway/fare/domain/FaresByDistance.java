package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;

import static java.lang.Integer.MAX_VALUE;

public enum FaresByDistance {
    BASIC(new Distance(1), new Distance(10), MAX_VALUE),
    SHORT(new Distance(11), new Distance(50), 5),
    LONG(new Distance(51), new Distance(MAX_VALUE), 8);

    public static final Fare BASIC_FARE = new Fare(1250);
    public static final Fare ADDITIONAL_FARE_UNIT = new Fare(100);
    private Distance moreDistance;
    private Distance belowDistance;
    private int distanceStandard;

    FaresByDistance(Distance moreDistance, Distance belowDistance, int distanceStandard) {
        this.moreDistance = moreDistance;
        this.belowDistance = belowDistance;
        this.distanceStandard = distanceStandard;
    }

    public static Fare calculate(Fare base, Distance distance) {
        int additionalFareDistance = distance.getDividedValue(findDistanceStandard(distance));

        return base.add(ADDITIONAL_FARE_UNIT.multiply(new Rate(additionalFareDistance)));
    }

    private static Integer findDistanceStandard(Distance distance) {
        return Arrays.stream(values())
                .filter(value -> distance.isMoreBelowThan(value.moreDistance, value.belowDistance))
                .map(value -> value.distanceStandard)
                .findFirst().orElseThrow(() -> new IllegalArgumentException("유효한 처리가 아닙니다." + distance));
    }
}
