package nextstep.subway.path.domain.fare;

import java.util.Collections;
import java.util.List;

public class FareCalculator {
    private static final int NO_FARE = 0;
    private static final int BASIC_FARE = 1250;
    private static final int ZERO_KM_DISTANCE = 0;
    private static final int FIVE_KM_DISTANCE = 5;
    private static final int EIGHT_KM_DISTANCE = 8;
    private static final int TEN_KM_DISTANCE = 10;
    private static final int FIFTY_KM_DISTANCE = 50;
    private static final int MIDDLE_KM_DISTANCE = FIFTY_KM_DISTANCE - TEN_KM_DISTANCE;

    public static int calculateFare(int distance, List<Integer> surcharges) {
        int fare = NO_FARE;
        fare += calculateDistanceFare(distance);
        fare += getMaxLineFare(surcharges);
        return fare;
    }

    private static int calculateDistanceFare(int distance) {
        int fare = BASIC_FARE;
        if (FIFTY_KM_DISTANCE < distance) {
            fare += calculateOverFare(MIDDLE_KM_DISTANCE, FIVE_KM_DISTANCE) +
                    calculateOverFare(distance - FIFTY_KM_DISTANCE, EIGHT_KM_DISTANCE);
            return fare;
        }
        fare += calculateOverFare(distance - TEN_KM_DISTANCE, FIVE_KM_DISTANCE);
        return fare;
    }

    private static int calculateOverFare(int distance, int kmByNumber) {
        if (distance < ZERO_KM_DISTANCE) {
            return NO_FARE;
        }
        return (int) ((Math.ceil((distance - 1) / kmByNumber) + 1) * 100);
    }

    private static int getMaxLineFare(List<Integer> surcharges) {
        if (surcharges.isEmpty()) {
            return NO_FARE;
        }
        return Collections.max(surcharges);
    }
}
