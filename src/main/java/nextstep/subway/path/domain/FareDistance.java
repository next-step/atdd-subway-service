package nextstep.subway.path.domain;

public class FareDistance {

    private static final int DISTANCE_LOWER_BOUND = 0;
    private static final int SHORT_DISTANCE_UPPER_BOUND = 10;
    private static final int LONG_DISTANCE_LOWER_BOUND = 50;
    private static final int BASE_DISTANCE_UNIT = 0;
    private static final int MID_DISTANCE_UNIT = 5;
    private static final int LONG_DISTANCE_UNIT = 8;

    private final int distance;

    public FareDistance(final int distance) {
        validate(distance);
        this.distance = distance;
    }

    private void validate(final int distance) {
        if (distance < DISTANCE_LOWER_BOUND) {
            throw new IllegalArgumentException("거리는 " + DISTANCE_LOWER_BOUND + "보다 크거나 같아야 합니다.");
        }
    }

    public int calculateDistanceUnit() {
        if (isLongDistance()) {
            return (distance - LONG_DISTANCE_LOWER_BOUND) / LONG_DISTANCE_UNIT;
        }
        if (isMidDistance()) {
            return (distance - SHORT_DISTANCE_UPPER_BOUND) / MID_DISTANCE_UNIT;
        }
        return BASE_DISTANCE_UNIT;
    }

    private boolean isLongDistance() {
        return distance > LONG_DISTANCE_LOWER_BOUND;
    }

    private boolean isMidDistance() {
        return distance > SHORT_DISTANCE_UPPER_BOUND && distance <= LONG_DISTANCE_LOWER_BOUND;
    }
}
