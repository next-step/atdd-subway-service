package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;
import org.springframework.stereotype.Component;

@Component
public class DistanceFarePolicy implements FarePolicy {
    private static final Fare BASIC_FARE = Fare.from(1_250);
    private static final Distance BASIC_DISTANCE = new Distance(10);
    private static final Distance EXTRA_FARE_DISTANCE = new Distance(50);

    @Override
    public Fare calculate(Distance distance) {
        if (distance.isLessThan(BASIC_DISTANCE)) {
            return BASIC_FARE;
        }
        if (distance.isLessThan(EXTRA_FARE_DISTANCE)) {
            return getAdditionalCharge5KM(distance);
        }

        return getAdditionalCharge8KM(distance);
    }

    private Fare getAdditionalCharge5KM(Distance distance) {
        Distance extraDistance = distance.minus(BASIC_DISTANCE);

        return Fare.from(BASIC_FARE.getValue() + (int) ((Math.ceil((extraDistance.getDistance() - 1) / 5) + 1) * 100));
    }

    private Fare getAdditionalCharge8KM(Distance distance) {
        Fare additionalCharge5KM = getAdditionalCharge5KM(distance);
        Distance extraDistance = distance.minus(EXTRA_FARE_DISTANCE);

        return Fare.from(additionalCharge5KM.getValue() + (int) ((Math.ceil((extraDistance.getDistance() - 1) / 8) + 1) * 100));
    }
}
