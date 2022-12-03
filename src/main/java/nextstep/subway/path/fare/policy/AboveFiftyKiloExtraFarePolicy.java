package nextstep.subway.path.fare.policy;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.fare.Fare;

public class AboveFiftyKiloExtraFarePolicy implements FarePolicy {

    private static final Distance MIN_DISTANCE = Distance.valueOf(50);
    private static final Distance DIVIDEND = Distance.valueOf(8);
    private static final Fare EXTRA_FARE = Fare.valueOf(100);

    @Override
    public Fare calculateFare(Distance distance) {
        if (distance.isLessThan(MIN_DISTANCE)) {
            return Fare.ZERO;
        }

        Distance extraDistance = getExtraDistance(distance);
        return EXTRA_FARE.multiply(extraDistance.divide(DIVIDEND).toInt());
    }

    private Distance getExtraDistance(Distance distance) {
        return distance.minus(MIN_DISTANCE);
    }
}
