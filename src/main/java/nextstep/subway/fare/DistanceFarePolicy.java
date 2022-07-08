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
            return applyMiddleDistanceExtraCharge(distance);
        }
        return applyLongDistanceExtraCharge(distance);
    }

    private Fare applyMiddleDistanceExtraCharge(Distance distance) {
        Distance extraDistance = distance.minus(BASIC_DISTANCE);
        return Fare.createMiddleDistanceExtraCharge(extraDistance);
    }

    private Fare applyLongDistanceExtraCharge(Distance distance) {
        Fare additionalCharge5KM = applyMiddleDistanceExtraCharge(distance);
        Distance extraDistance = distance.minus(EXTRA_FARE_DISTANCE);
        return Fare.createLongDistanceExtraCharge(additionalCharge5KM, extraDistance);
    }
}
