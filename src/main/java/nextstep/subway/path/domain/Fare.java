package nextstep.subway.path.domain;

import java.math.BigDecimal;

public class Fare {
    private BigDecimal fare;
    private AgeTypeFare ageTypeFare;

    public Fare(int maxOverFare, int age, int distance) {
        ageTypeFare = AgeTypeFare.valueOf(age);
        int overFare = findOverFare(distance, maxOverFare);
        fare = ageTypeFare.caculate(BigDecimal.valueOf(overFare));
    }

    private int findOverFare(int distance, int maxOverFare) {
        int distanceFare = calculateOverFare(distance);
        return distanceFare + maxOverFare;
    }

    private int calculateOverFare(int distance) {
        DistanceTypeFare distanceTypeFare = DistanceTypeFare.valueOf(distance);
        return distanceTypeFare.calculate(distance);
    }

    public BigDecimal getFare() {
        return fare;
    }
}
