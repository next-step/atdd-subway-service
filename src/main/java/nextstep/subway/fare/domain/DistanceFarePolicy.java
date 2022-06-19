package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;
import org.springframework.stereotype.Component;

@Component
public class DistanceFarePolicy {
    private static final Fare BASIC_FARE = Fare.from(1_250);
    private static final Distance BASIC_DISTANCE = Distance.from(10);
    private static final Distance PREMIUM_DISTANCE = Distance.from(50);


    public Fare calculate(Distance distance) {
        if (distance.isOrLess(BASIC_DISTANCE)) {
            return BASIC_FARE;
        }

        if (distance.isOrLess(PREMIUM_DISTANCE)) {
            return getStandardFare(distance);
        }

        return getPremiumFare(distance);
    }

    private Fare getStandardFare(Distance distance) {
        Distance extraDistance = distance.minus(BASIC_DISTANCE);

        return Fare.from(
                (Math.ceil((extraDistance.getValue() - 1) / 5) + 1) * 100 + BASIC_FARE.getValue().doubleValue()
        );
    }

    private Fare getPremiumFare(Distance distance) {
        Fare standardFullFare = getStandardFare(PREMIUM_DISTANCE);
        Distance extraDistance = distance.minus(PREMIUM_DISTANCE);

        return Fare.from(
                (Math.ceil((extraDistance.getValue() - 1) / 8) + 1) * 100 + standardFullFare.getValue().doubleValue()
        );
    }
}
