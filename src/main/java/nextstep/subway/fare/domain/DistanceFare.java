package nextstep.subway.fare.domain;

import static nextstep.subway.fare.domain.Fare.ADDITIONAL;
import static nextstep.subway.fare.domain.Fare.BASIC;

public class DistanceFare implements FarePolicy {
    private static final int DEFAULT_CHARGE = 1250;

    @Override
    public int calculateFare(int distance) {
        int fare = DEFAULT_CHARGE;

        if (ADDITIONAL.isLonger(distance)) {
            fare += ADDITIONAL.calculateOverFare(distance);
            distance = ADDITIONAL.getDistance();
        }
        if (BASIC.isLonger(distance)) {
            fare += BASIC.calculateOverFare(distance);
        }

        return fare;
    }
}
