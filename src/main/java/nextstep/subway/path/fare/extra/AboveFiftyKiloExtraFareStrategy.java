package nextstep.subway.path.fare.extra;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.fare.Fare;

public class AboveFiftyKiloExtraFareStrategy implements ExtraFareStrategy {

    private static final Distance MIN_DISTANCE = Distance.valueOf(50);
    private static final Fare EXTRA_FARE = Fare.valueOf(100);
    private static final int DIVIDEND = 8;

    @Override
    public Fare calculate(Path path) {
        Distance distance = path.getDistance();
        if (distance.isLessThanOrEqualTo(MIN_DISTANCE)) {
            return Fare.ZERO;
        }

        Distance extraDistance = getExtraDistance(distance);
        return EXTRA_FARE.multiply(extraDistance.toInt() / DIVIDEND);
    }

    private Distance getExtraDistance(Distance distance) {
        return distance.minus(MIN_DISTANCE);
    }
}
