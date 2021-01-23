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
        if (distance > 10 && distance < 50) {
            distance = distance - 10;
            return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
        }

        if (distance >= 50) {
            distance = distance -50;
            return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }

        return 0;
    }

    public BigDecimal getFare() {
        return fare;
    }
}
