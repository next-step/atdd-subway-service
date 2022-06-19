package nextstep.subway.line.domain;

import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE = 1_250;

    private final int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public static Fare calculate(Distance distance, Integer age, int surcharge) {
        int fare = DEFAULT_FARE + surcharge;
        fare += calculateDistanceFare(distance);
        fare = calculateDiscount(age, fare);
        return new Fare(fare);
    }

    private static int calculateDistanceFare(Distance distance) {
        return DistanceType.of(distance).calculateDistanceFare(distance.toInt());
    }

    private static int calculateDiscount(Integer age, int fare) {
        return Discount.of(age).calculate(fare);
    }

    private static int calculateOverFareAbove50(int distance) {
        if (distance <= 50) {
            return 0;
        }
        distance = distance - 50;
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    public int toInt() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
