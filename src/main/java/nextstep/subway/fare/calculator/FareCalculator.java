package nextstep.subway.fare.calculator;

import static nextstep.subway.fare.domain.FareType.*;
import static nextstep.subway.fare.domain.FareType.BASIC;

import nextstep.subway.fare.domain.FareSectionType;

public class FareCalculator {
    private static final int DISTANCE_0 = 0;
    private static final int NO_ADDITIONAL_FARE = 0;

    private FareCalculator() {}

    public static int calculate(int distance) {
        FareSectionType fareSectionType = FareSectionType.findByDistance(distance);
        if (fareSectionType.isBasic()) {
            return BASIC.getFare();
        }

        return calculateInitFare(fareSectionType) + calculateOverFare(distance, fareSectionType);
    }

    private static int calculateInitFare(FareSectionType sectionType) {
        return BASIC.getFare() + (sectionType.getAdditionalCount() * ADDITIONAL.getFare());
    }

    private static int calculateOverFare(int distance, FareSectionType fareSectionType) {
        double overDistance = distance - fareSectionType.getMinDistance();
        if (overDistance <= DISTANCE_0) {
            return NO_ADDITIONAL_FARE;
        }

        return (int) (Math.ceil(overDistance / fareSectionType.getAdditionalDistanceUnit()) * ADDITIONAL.getFare());
    }
}
