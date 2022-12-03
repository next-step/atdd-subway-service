package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

public class FareCalculator {

    public static final int DEFAULT_FARE = 1_250;

    public static int calculate(int distance, int age, int extraFare) {
        Fare fare = Fare.from(DEFAULT_FARE)
                .add(Fare.from(extraFare))
                .add(FareDistanceCalculator.calculate(distance));
        return fare.subtract(FareAgeCalculator.calculate(fare.getValue(), age)).getValue();
    }
}
