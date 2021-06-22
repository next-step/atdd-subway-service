package nextstep.subway.path.domain;

import java.util.List;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.line.domain.Line;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final int fareByDistance;
    private final int surcharge;
    private final DiscountStrategy discountStrategy;

    public Fare(Distance distance, List<Line> lines, DiscountStrategy discountStrategy) {
        this.fareByDistance = calculateFare(distance);
        this.surcharge = extractMaxSurcharge(lines);
        this.discountStrategy = discountStrategy;
    }

    public int getTotalFare() {
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

    private int extractMaxSurcharge(List<Line> lines) {
        return lines.stream()
                    .mapToInt(Line::getSurcharge)
                    .max()
                    .orElse(0);
    }
}
