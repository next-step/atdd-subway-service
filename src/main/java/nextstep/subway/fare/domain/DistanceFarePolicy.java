package nextstep.subway.fare.domain;

import nextstep.subway.line.domain.Distance;

public class DistanceFarePolicy {
    private static final Fare BASIC_FARE = Fare.from(1250);
    private static final Distance BASIC_DISTANCE = Distance.from(10);
    private static final Distance PREMIUM_DISTANCE = Distance.from(50);


    public Fare calculate(Distance distance) {
        if (distance.compareTo(BASIC_DISTANCE) != 1) {
            return BASIC_FARE;
        }

        if (distance.compareTo(PREMIUM_DISTANCE) != 1) {
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

        Fare standardFullFare = Fare.from(
                (Math.ceil((PREMIUM_DISTANCE.minus(BASIC_DISTANCE).getValue() - 1) / 5) + 1) * 100
                        + BASIC_FARE.getValue().doubleValue()
        );

        Distance extraDistance = distance.minus(Distance.from(50));

        return Fare.from(
                (Math.ceil((extraDistance.getValue() - 1) / 8) + 1) * 100 + standardFullFare.getValue().doubleValue()
        );
    }
}
