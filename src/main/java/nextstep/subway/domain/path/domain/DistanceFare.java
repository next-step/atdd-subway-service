package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.line.domain.Line;
import nextstep.subway.domain.line.domain.Section;

public class DistanceFare {

    private static final int DEFAULT_AMOUNT = 1250;
    private static final int EXTRA_AMOUNT = 100;
    public static final int DEFAULT_AMOUNT_DISTANCE = 10;
    private static final int ONE_STEP_EXTRA_FARE_MAX_DISTANCE = 50;
    private static final float ONE_STEP_EXTRA_FARE_DISTANCE = 5f;
    private static final float TWO_STEP_EXTRA_FARE_DISTANCE = 8f;
    private Route route;

    DistanceFare() {
    }

    DistanceFare(Route route) {
        this.route = route;
    }

    public Amount calculateAmount() {
        int distance = route.getDistance().getDistance();

        if (isExtraDistance(distance)) {
            return new Amount(getExtraAmount(distance)).plus(lineFareCalculateAmount());
        }

        return new Amount(DEFAULT_AMOUNT);
    }

    private int getExtraAmount(int distance) {
        int extraAmount = calculateExtraDistanceFare(stepOneExcessDistance(distance), ONE_STEP_EXTRA_FARE_DISTANCE);

        if (isStepTwoExcessDistance(distance)) {
            extraAmount += calculateExtraDistanceFare(stepTwoExcessDistance(distance), TWO_STEP_EXTRA_FARE_DISTANCE);
        }

        extraAmount += DEFAULT_AMOUNT;
        return extraAmount;
    }

    private Amount lineFareCalculateAmount() {
        int lineFare = route.getSections().stream()
                .map(Section::getLine)
                .mapToInt(Line::getExtraFare)
                .max()
                .getAsInt();

        return new Amount(lineFare);
    }

    private static int stepOneExcessDistance(final int distance) {
        if (distance <= ONE_STEP_EXTRA_FARE_MAX_DISTANCE) {
            return distance - DEFAULT_AMOUNT_DISTANCE;
        }
        return ONE_STEP_EXTRA_FARE_MAX_DISTANCE - DEFAULT_AMOUNT_DISTANCE;
    }

    private static boolean isStepTwoExcessDistance(final int distance) {
        return distance > ONE_STEP_EXTRA_FARE_MAX_DISTANCE;
    }

    private static int stepTwoExcessDistance(final int distance) {
        return distance - ONE_STEP_EXTRA_FARE_MAX_DISTANCE;
    }

    private static int calculateExtraDistanceFare(int excessDistance, float stepByStepExtraFareDistance) {
        return (int) (Math.ceil(excessDistance / stepByStepExtraFareDistance)) * EXTRA_AMOUNT;
    }

    private static boolean isExtraDistance(int distance) {
        return distance > DEFAULT_AMOUNT_DISTANCE;
    }
}
