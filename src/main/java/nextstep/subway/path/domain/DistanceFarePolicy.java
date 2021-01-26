package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceFarePolicy {
    DEFAULT {
        @Override
        public int calculateFare(int distance) {
            return DEFAULT_FARE;
        }

        @Override
        public boolean condition(int distance) {
            return distance <= DEFAULT_MAX_DISTANCE;
        }
    },
    UNDER_FIFTY {
        @Override
        public int calculateFare(int distance) {
            int extraArea = distance - DEFAULT_MAX_DISTANCE;
            return DEFAULT_FARE
                    + (int) ((Math.ceil((extraArea - 1) / FIRST_EXTRA_CHARGE_UNIT) + 1) * EXTRA_CHARGE);
        }

        @Override
        public boolean condition(int distance) {
            return distance > DEFAULT_MAX_DISTANCE && distance <= SECOND_MAX_DISTANCE;
        }
    },
    OVER_FIFTY {
        @Override
        public int calculateFare(int distance) {
            int extraArea = distance - SECOND_MAX_DISTANCE;
            return UNDER_FIFTY.calculateFare(SECOND_MAX_DISTANCE)
                    + (int) ((Math.ceil((extraArea - 1) / SECOND_EXTRA_CHARGE_UNIT) + 1) * EXTRA_CHARGE);
        }

        @Override
        public boolean condition(int distance) {
            return distance > SECOND_MAX_DISTANCE;
        }
    },
    UNKNOWN;

    private static final int ZERO = 0;
    private static final int DEFAULT_FARE = 1250;
    private static final int EXTRA_CHARGE = 100;
    private static final int DEFAULT_MAX_DISTANCE = 10;
    private static final int SECOND_MAX_DISTANCE = 50;
    private static final int FIRST_EXTRA_CHARGE_UNIT = 5;
    private static final int SECOND_EXTRA_CHARGE_UNIT = 8;

    public int calculateFare(int distance) {
        return ZERO;
    }

    public boolean condition(int distance) {
        return false;
    }

    public static DistanceFarePolicy getPolicy(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distanceFarePolicy.condition(distance))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
