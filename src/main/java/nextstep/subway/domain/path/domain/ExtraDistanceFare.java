package nextstep.subway.domain.path.domain;

public class ExtraDistanceFare {

    private static final int EXTRA_AMOUNT = 100;
    private static final int ONE_STEP_EXTRA_FARE_MAX_DISTANCE = 50;
    private static final float ONE_STEP_EXTRA_FARE_DISTANCE = 5f;
    private static final float TWO_STEP_EXTRA_FARE_DISTANCE = 8f;

    private ExtraDistanceFare() {
    }

    public static int extraAmount(final int distance) {
        int extraAmount = extraAmount(stepOneExcessDistance(distance), ONE_STEP_EXTRA_FARE_DISTANCE);

        if (isStepTwoExcessDistance(distance)) {
            extraAmount += extraAmount(stepTwoExcessDistance(distance), TWO_STEP_EXTRA_FARE_DISTANCE);
        }

        return extraAmount;
    }

    private static int stepOneExcessDistance(final int distance) {
        if (distance <= ONE_STEP_EXTRA_FARE_MAX_DISTANCE) {
            return distance - Fare.DEFAULT_AMOUNT_DISTANCE;
        }
        return ONE_STEP_EXTRA_FARE_MAX_DISTANCE - Fare.DEFAULT_AMOUNT_DISTANCE;
    }

    private static boolean isStepTwoExcessDistance(final int distance) {
        return distance > ONE_STEP_EXTRA_FARE_MAX_DISTANCE;
    }

    private static int stepTwoExcessDistance(final int distance) {
        return distance - ONE_STEP_EXTRA_FARE_MAX_DISTANCE;
    }

    private static int extraAmount(int excessDistance, float stepByStepExtraFareDistance) {
        return (int) (Math.ceil(excessDistance / stepByStepExtraFareDistance)) * EXTRA_AMOUNT;
    }
}
