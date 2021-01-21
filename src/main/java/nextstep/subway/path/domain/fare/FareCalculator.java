package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.fare.distance.DistanceFare;
import nextstep.subway.path.domain.fare.distance.DistanceFareFactory;

import java.util.Collections;
import java.util.List;

public class FareCalculator {
    private static final int NO_FARE = 0;
    private static final int BASIC_FARE = 1250;

    public static int calculateFare(int distance, List<Integer> surcharges) {
        int fare = NO_FARE;
        fare += calculateDistanceFare(distance);
        fare += getMaxLineFare(surcharges);
        return fare;
    }

    private static int calculateDistanceFare(int distance) {
        DistanceFare distanceFare = DistanceFareFactory.instance().getDistanceFare(distance);
        return BASIC_FARE + distanceFare.calculateFare();
    }

    private static int getMaxLineFare(List<Integer> surcharges) {
        if (surcharges.isEmpty()) {
            return NO_FARE;
        }
        return Collections.max(surcharges);
    }
}
