package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;
import org.springframework.stereotype.Component;

@Component
public class DistanceFarePolicy {
    private static final Fare BASIC_FARE = Fare.of(1_250);
    private static final Fare EXTRA_FARE = Fare.of(100);
    private static final Distance BASIC_DISTANCE = Distance.of(10);
    private static final Distance EXTRA_FARE_DISTANCE = Distance.of(50);
    private static final Distance BASIC_DISTANCE_PER_FARE = Distance.of(5);
    private static final Distance EXTRA_FARE_DISTANCE_PER_FARE = Distance.of(8);

    public Fare calculate(Distance distance) {
        if (!distance.isLonger(BASIC_DISTANCE)) {
            return BASIC_FARE;
        }
        if (!distance.isLonger(EXTRA_FARE_DISTANCE)) {
            return BASIC_FARE.plus(getExtraFareEvery5KM(distance));
        }
        return BASIC_FARE.plus(getExtraFareEvery8KM(distance));
    }

    private Fare getExtraFareEvery5KM(Distance distance) {
        Distance extraDistance = distance.minus(BASIC_DISTANCE);
        return Fare.of((int) ((Math.ceil((extraDistance.getDistance() - 1) / BASIC_DISTANCE_PER_FARE.getDistance()) + 1) * EXTRA_FARE.getFare()));
    }

    private Fare getExtraFareEvery8KM(Distance distance) {
        Fare extraFare5KM = getExtraFareEvery5KM(EXTRA_FARE_DISTANCE);
        Distance extraDistance = distance.minus(EXTRA_FARE_DISTANCE);
        return Fare.of(extraFare5KM.getFare() + (int) ((Math.ceil((extraDistance.getDistance() - 1) / EXTRA_FARE_DISTANCE_PER_FARE.getDistance()) + 1) * EXTRA_FARE.getFare()));
    }
}
