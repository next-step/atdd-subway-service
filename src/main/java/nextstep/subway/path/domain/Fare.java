package nextstep.subway.path.domain;

import java.util.Collection;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final int fareByDistance;
    private final int surcharge;

    public Fare(Distance distance, BelongLines belongLines) {
        this.fareByDistance = calculateFare(distance);
        this.surcharge = extractMaxSurcharge(belongLines.getLines());
    }

    public int getTotalFare(DiscountStrategy discountStrategy) {
        return discountStrategy.discount(fareByDistance + surcharge);
    }

    private int calculateFare(Distance distance) {

        int value = distance.getValue();
        int fare = DEFAULT_FARE;

        if (value > 10) {
            fare += additionalFare(Math.min(value - 10, 40), 5);
        }

        if (value > 50) {
            fare += additionalFare(value - 50, 8);
        }

        return fare;
    }

    private int additionalFare(int distance, int perDistance) {
        return ((distance - 1) / perDistance + 1) * 100;
    }

    private int extractMaxSurcharge(Collection<Line> lines) {
        return lines.stream()
                    .mapToInt(Line::getSurcharge)
                    .max()
                    .orElse(0);
    }
}
