package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.DistanceFarePolicy.DistanceFarePolicyConstants.*;

import java.util.Arrays;
import java.util.function.Function;

public enum DistanceFarePolicy {
    LOW(1, 10,
            distance -> DEFAULT_FARE),
    MIDDLE(11, 50,
            distance -> DEFAULT_FARE + calculateOverFare(distance, 11, MIDDLE_UNIT, OVERFARE_PER_UNIT)),
    HIGH(51, 178,
            distance -> DEFAULT_HIGH_FARE + calculateOverFare(distance, 51, HIGH_UNIT, OVERFARE_PER_UNIT));

    public static class DistanceFarePolicyConstants {
        public static final int OVERFARE_PER_UNIT = 100;
        public static final int OVERFARE_PER_UNIT_MIN = 0;
        public static final int OVERFARE_MIN = 0;
        public static final int MIDDLE_UNIT = 5;
        public static final int HIGH_UNIT = 8;
        public static final int DEFAULT_FARE = 1250;
        public static final int DEFAULT_HIGH_FARE = DEFAULT_FARE + (int) (
                (Math.ceil((MIDDLE.endDistance - MIDDLE.startDistance) / MIDDLE_UNIT) + 1)
                        * OVERFARE_PER_UNIT);
    }

    private final int startDistance;
    private final int endDistance;
    private final Function<Integer, Integer> expression;

    DistanceFarePolicy(int startDistance, int endDistance, Function<Integer, Integer> expression) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.expression = expression;
    }

    public static DistanceFarePolicy findByDistance(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFareType -> distanceFareType.isIncludedRange(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("비정상적인 이동 거리입니다."));
    }

    private boolean isIncludedRange(int distance) {
        return distance >= startDistance && distance <= endDistance;
    }

    public int calculate(int distance) {
        return expression.apply(distance);
    }

    private static int calculateOverFare(int distance, int startDistance, int calculateUnit, int overFarePerUnit) {
        int fare = OVERFARE_MIN;
        if (overFarePerUnit > OVERFARE_PER_UNIT_MIN) {
            int calculateDistance = distance - startDistance + 1;
            fare = (int) ((Math.ceil((calculateDistance - 1) / calculateUnit) + 1) * overFarePerUnit);
        }
        return fare;
    }
}
