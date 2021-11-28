package nextstep.subway.fare.calculator;

import static nextstep.subway.fare.domain.FareType.*;
import static nextstep.subway.fare.domain.FareType.BASIC;

import nextstep.subway.fare.domain.FareSectionType;

public class FareCalculator {
    private static final int DISTANCE_0 = 0;
    private static final int NO_FARE = 0;

    private FareCalculator() {}

    public static int calculate(int distance) {
        if (distance == DISTANCE_0) {
            return NO_FARE;
        }

        FareSectionType fareSectionType = FareSectionType.findByDistance(distance);
        return calculateInitFare(fareSectionType) + calculateOverFare(distance, fareSectionType);
    }

    private static int calculateInitFare(FareSectionType sectionType) {
        if (sectionType.isBasic()) {
            return BASIC.getFare();
        }

        return BASIC.getFare() + (sectionType.getAdditionalCount() * ADDITIONAL.getFare());
    }

    private static int calculateOverFare(int distance, FareSectionType fareSectionType) {
        double overDistance = distance - fareSectionType.getMinDistance();
        if (fareSectionType.isBasic() || overDistance <= DISTANCE_0) {
            return NO_FARE;
        }

        return (int) (Math.ceil(overDistance / fareSectionType.getAdditionalDistanceUnit()) * ADDITIONAL.getFare());
    }
}
