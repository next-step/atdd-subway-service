package nextstep.subway.path.fare;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.domain.Fare;

public class BasicFarePolicy implements FarePolicy {

    public static final Distance MAX_DISTANCE = Distance.valueOf(10);
    public static final Fare BASIC_FARE = Fare.valueOf(1_250);

    @Override
    public Fare calculateFare(Distance distance) {
        return BASIC_FARE;
    }
}
