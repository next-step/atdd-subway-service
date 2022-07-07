package nextstep.subway.fare;

import org.springframework.stereotype.Component;

@Component
public class DistanceFarePolicy {
    private static final Distance BASIC_DISTANCE = new Distance(10);
    private static final Distance EXTRA_FARE_DISTANCE = new Distance(50);

    public Fare calculate(Distance distance) {
        if (distance.isLessThan(BASIC_DISTANCE)) {
            return Fare.basic();
        }
        if (distance.isLessThan(EXTRA_FARE_DISTANCE)) {
            return apply5kmExtraCharge(distance);
        }
        return apply8kmExtraCharge(distance);
    }

    private Fare apply5kmExtraCharge(Distance distance) {
        Distance extraDistance = distance.minus(BASIC_DISTANCE);
        return Fare.create5kmExtraCharge(extraDistance);
    }

    private Fare apply8kmExtraCharge(Distance distance) {
        Fare additionalCharge5KM = apply5kmExtraCharge(distance);
        Distance extraDistance = distance.minus(EXTRA_FARE_DISTANCE);
        return Fare.create8kmExtraCharge(additionalCharge5KM, extraDistance);
    }
}
