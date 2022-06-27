package nextstep.subway.path.types;

public enum ChargeType {
    LEVEL_ONE_CHARGE {
        @Override
        public int calculateDistanceCharge(final int distance) {
            if (distance < 0 || distance > LEVEL_ONE_THRESHOLD) {
                throw new IllegalArgumentException();
            }
            return LEVEL_ONE_CHARGE_AMOUNT;
        }
    }, LEVEL_TWO_CHARGE {
        @Override
        public int calculateDistanceCharge(final int distance) {
            if (distance < LEVEL_ONE_THRESHOLD || distance > LEVEL_TWO_THRESHOLD) {
                throw new IllegalArgumentException();
            }
            final int levelTwoDistance = distance - LEVEL_ONE_THRESHOLD;
            return LEVEL_ONE_CHARGE_AMOUNT + calculateLevelTwoCharge(levelTwoDistance);
        }
    }, LEVEL_THREE_CHARGE {
        @Override
        public int calculateDistanceCharge(final int distance) {
            if (distance <= LEVEL_TWO_THRESHOLD) {
                throw new IllegalArgumentException();
            }
            final int levelTwoDistance = LEVEL_TWO_THRESHOLD - LEVEL_ONE_THRESHOLD;
            final int levelThreeDistance = distance - LEVEL_TWO_THRESHOLD;
            return LEVEL_ONE_CHARGE_AMOUNT + calculateLevelTwoCharge(levelTwoDistance)
                    + calculateLevelThreeCharge(levelThreeDistance);
        }
    };

    private static final int LEVEL_ONE_CHARGE_AMOUNT = 1_250;
    private static final int LEVEL_TWO_CHARGE_AMOUNT = 100;
    private static final int LEVEL_THREE_CHARGE_AMOUNT = 100;
    private static final int LEVEL_TWO_SECTION_DISTANCE = 5;
    private static final int LEVEL_THREE_SECTION_DISTANCE = 8;
    private static final int LEVEL_ONE_THRESHOLD = 10;
    private static final int LEVEL_TWO_THRESHOLD = 50;

    public static int calculateChargeFrom(final int distance) {
        if (distance <= LEVEL_ONE_THRESHOLD) {
            return LEVEL_ONE_CHARGE.calculateDistanceCharge(distance);
        }
        if (LEVEL_ONE_THRESHOLD < distance && distance <= LEVEL_TWO_THRESHOLD) {
            return LEVEL_TWO_CHARGE.calculateDistanceCharge(distance);
        }
        return LEVEL_THREE_CHARGE.calculateDistanceCharge(distance);
    }

    abstract public int calculateDistanceCharge(final int distance);

    private static int calculateLevelTwoCharge(final int distance) {
        return (int) ((Math.ceil((distance - 1) / LEVEL_TWO_SECTION_DISTANCE) + 1) * LEVEL_TWO_CHARGE_AMOUNT);
    }

    private static int calculateLevelThreeCharge(final int distance) {
        return (int) ((Math.ceil((distance - 1) / LEVEL_THREE_SECTION_DISTANCE) + 1) * LEVEL_THREE_CHARGE_AMOUNT);
    }
}
