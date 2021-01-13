package nextstep.subway.fare;

import java.util.Arrays;
import java.util.function.Function;

public enum FarePolicyByDistance {
    BASIC(0, 9, (distance) -> {
        return Fare.createBaseFare();
    }),
    SHORT_DISTANCE(10, 49, (distance) -> {
        return Fare.of(calculateLineFare(distance, 5));
    }),
    LONG_DISTANCE(50, Integer.MAX_VALUE, (distance) -> {
        return Fare.of(calculateLineFare(distance, 8));
    });

    private static final int ADDITIONAL_FARE = 100;
    private final int staringDistance;
    private final int endingDistance;
    private final Function<Integer, Fare> lineFareExpression;

    FarePolicyByDistance(final int staringDistance, final int endingDistance, final Function<Integer, Fare> lineFareExpression) {
        this.staringDistance = staringDistance;
        this.endingDistance = endingDistance;
        this.lineFareExpression = lineFareExpression;
    }

    private static int calculateLineFare(final int distance, final int baseDistance) {
        final int divided = (distance - 1) / baseDistance;
        return (int) ((Math.ceil(divided) + 1) * ADDITIONAL_FARE);
    }

    private static FarePolicyByDistance getLineFareType(final int distance) {
        return Arrays.stream(values())
            .filter(farePolicyByDistance -> farePolicyByDistance.staringDistance <= distance)
            .filter(farePolicyByDistance -> farePolicyByDistance.endingDistance >= distance)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static Fare calculateLineFare(final int distance) {
        return getLineFareType(distance).lineFareExpression.apply(distance);
    }
}
