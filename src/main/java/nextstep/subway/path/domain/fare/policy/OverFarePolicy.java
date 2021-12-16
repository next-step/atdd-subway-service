package nextstep.subway.path.domain.fare.policy;

import nextstep.subway.path.domain.fare.Fare;

public class OverFarePolicy {

    private static final int BASE_ZONE_MAX_DISTANCE = 10;
    private static final int SECOND_ZONE_MAX_DISTANCE = 50;

    private OverFarePolicy() {
    }

    public static Fare calculateFare(final Fare fare, final int distance) {
        if (distance <= BASE_ZONE_MAX_DISTANCE) {
            return fare;
        }

        if (distance <= SECOND_ZONE_MAX_DISTANCE) {
            return fare.add(calculateOverFare(distance - BASE_ZONE_MAX_DISTANCE, 5));
        }

        return fare.add(calculateOverFare(SECOND_ZONE_MAX_DISTANCE - BASE_ZONE_MAX_DISTANCE, 5))
            .add(calculateOverFare(distance - SECOND_ZONE_MAX_DISTANCE, 8));
    }

    private static int calculateOverFare(final int distance, final int overFarePer) {
        return (int) ((Math.ceil((distance - 1) / overFarePer) + 1) * 100);
    }
}
