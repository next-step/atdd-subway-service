package nextstep.subway.path.fare;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;

public class UntilFiftyKiloExtraFarePolicy implements FarePolicy {

    private static final Distance MIN_DISTANCE = Distance.valueOf(10);
    private static final Distance DIVIDEND = Distance.valueOf(5);
    private static final Fare EXTRA_FARE = Fare.valueOf(100);
    private static final Fare MAX_EXTRA_FARE = Fare.valueOf(800);

    @Override
    public Fare calculateFare(Distance distance) {
        if (distance.isLessThan(MIN_DISTANCE)) {
            return Fare.ZERO;
        }

        Distance extraDistance = getExtraDistance(distance);
        Fare extraFare = multiplyExtraFare(extraDistance);
        return extraFare.isGreaterThan(MAX_EXTRA_FARE) ? MAX_EXTRA_FARE : extraFare;
    }

    private Fare multiplyExtraFare(Distance extraDistance) {
        return EXTRA_FARE.multiply(extraDistance.divide(DIVIDEND).toInt());
    }

    private Distance getExtraDistance(Distance distance) {
        return distance.minus(BasicFarePolicy.MAX_DISTANCE);
    }
}
