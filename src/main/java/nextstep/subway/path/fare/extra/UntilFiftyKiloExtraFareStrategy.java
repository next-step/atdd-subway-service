package nextstep.subway.path.fare.extra;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;

public class UntilFiftyKiloExtraFareStrategy implements ExtraFareStrategy {

    private static final Distance MIN_DISTANCE = Distance.valueOf(10);
    private static final Fare EXTRA_FARE = Fare.valueOf(100);
    private static final Fare MAX_EXTRA_FARE = Fare.valueOf(800);
    private static final int DIVIDEND = 5;

    @Override
    public Fare calculate(Path path) {
        Distance distance = path.getDistance();
        if (distance.isLessThanOrEqualTo(MIN_DISTANCE)) {
            return Fare.ZERO;
        }

        Distance extraDistance = getExtraDistance(distance);
        Fare extraFare = multiplyExtraFare(extraDistance);
        return extraFare.isGreaterThan(MAX_EXTRA_FARE) ? MAX_EXTRA_FARE : extraFare;
    }

    private Fare multiplyExtraFare(Distance extraDistance) {
        return EXTRA_FARE.multiply(extraDistance.toInt() / DIVIDEND);
    }

    private Distance getExtraDistance(Distance distance) {
        if (distance.isLessThanOrEqualTo(BasicExtraFareStrategy.MAX_DISTANCE)) {
            return Distance.ZERO;
        }
        return distance.minus(BasicExtraFareStrategy.MAX_DISTANCE);
    }
}
