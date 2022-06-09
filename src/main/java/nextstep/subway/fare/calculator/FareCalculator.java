package nextstep.subway.fare.calculator;

import nextstep.subway.fare.domain.FareSectionType;
import nextstep.subway.fare.domain.FareType;

public class FareCalculator {

    private static final int ZERO = 0;

    public static int calculate(int distance) {
        FareSectionType fareSectionType = FareSectionType.findTypeByDistance(distance);
        return FareType.BASIC.getFare() + calculateOverFare(distance, fareSectionType);
    }

    private static int calculateOverFare(int distance, FareSectionType fareSectionType) {
        if (fareSectionType == FareSectionType.BASIC) {
            return ZERO;
        }

        return (int) ((Math.ceil((fareSectionType.findOverDistance(distance) - 1) / fareSectionType.getAdditionalFareUnit()) + 1) * FareType.ADDITION.getFare());
    }
}
