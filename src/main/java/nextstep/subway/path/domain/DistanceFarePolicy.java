package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFarePolicy {
    NONE(
            0,
            9,
            distance -> Fare.from(0)),
    BETWEEN_11_AND_50_EXTRA_FARE(
            11,
            50,
            distance -> Fare.from(calculateFiveIntervalExtraFare(distance))),
    BETWEEN_50_AND_MAX_EXTRA_FARE(
            51,
            Integer.MAX_VALUE,
            distance -> Fare.from(calculateEightIntervalExtraFare(distance)));

    private static final int EXTRA_FARE = 100;
    private static final int BASE_DISTANCE = 10;
    private static final int FIVE_INTERVAL_MAX_DISTANCE = 50;

    private final int from;
    private final int to;
    private final Function<Integer, Fare> fareFunction;

    DistanceFarePolicy(int from, int to, Function<Integer, Fare> fareFunction) {
        this.from = from;
        this.to = to;
        this.fareFunction = fareFunction;
    }

    public static Fare valueOf(int distance) {
        return getDistanceFarePolicy(distance).fareFunction.apply(distance);
    }

    private static DistanceFarePolicy getDistanceFarePolicy(int distance) {
        return Arrays.stream(values())
                .filter(distanceFarePolicy -> distanceFarePolicy.contain(distance))
                .findFirst()
                .orElse(NONE);
    }

    private static int calculateFiveIntervalExtraFare(int distance) {
        double extraDistance = Math.ceil((double) (distance - BASE_DISTANCE) / 5);
        return (int) extraDistance * EXTRA_FARE;
    }

    private static int calculateEightIntervalExtraFare(Integer distance) {
        double extraDistance = Math.ceil((double) (distance - FIVE_INTERVAL_MAX_DISTANCE) / 8);
        return (int) extraDistance * EXTRA_FARE + calculateFiveIntervalExtraFare(FIVE_INTERVAL_MAX_DISTANCE);
    }

    private boolean contain(int distance) {
        return this.from <= distance && this.to >= distance;
    }
}
