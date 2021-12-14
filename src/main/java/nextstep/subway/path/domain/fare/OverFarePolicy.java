package nextstep.subway.path.domain.fare;

public class OverFarePolicy {

    private static final int ZERO = 0;
    private static final int BASE_ZONE_MAX_DISTANCE = 10;
    private static final int SECOND_ZONE_MAX_DISTANCE = 50;

    private OverFarePolicy() {
    }

    public static int calculateOverFare(final int distance) {
        if (distance <= BASE_ZONE_MAX_DISTANCE) {
            return ZERO;
        }

        if (distance <= SECOND_ZONE_MAX_DISTANCE) {
            return calculateOverFare(distance - BASE_ZONE_MAX_DISTANCE, 5);
        }

        return calculateOverFare(SECOND_ZONE_MAX_DISTANCE - BASE_ZONE_MAX_DISTANCE, 5)
            + calculateOverFare(distance - SECOND_ZONE_MAX_DISTANCE, 8);
    }

    private static int calculateOverFare(final int distance, final int overFarePer) {
        return (int) ((Math.ceil((distance - 1) / overFarePer) + 1) * 100);
    }
}
