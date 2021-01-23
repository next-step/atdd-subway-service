package nextstep.subway.path.domain;

import java.math.BigDecimal;

public class Fare {
    private BigDecimal fare;
    private AgeTypeFare ageTypeFare;

    public Fare(int age, int distance) {
        ageTypeFare = AgeTypeFare.valueOf(age);
        int distanceFare = calculateOverFare(distance);
        fare = ageTypeFare.caculate(BigDecimal.valueOf(distanceFare));
    }

    private int calculateOverFare(int distance) {
        DistanceTypeFare distanceTypeFare = DistanceTypeFare.valueOf(distance);
        return distanceTypeFare.calculate(distance);
    }

    public BigDecimal getFare() {
        return fare;
    }
}
