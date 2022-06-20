package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE = 1_250;

    private final int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public static Fare calculate(Distance distance, Integer age, int surcharge) {
        int fare = DEFAULT_FARE + surcharge;
        fare += distance.calculateFare();
        fare = calculateDiscount(age, fare);
        return new Fare(fare);
    }

    private static int calculateDiscount(Integer age, int fare) {
        return AgeDiscountType.of(age).calculate(fare);
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
