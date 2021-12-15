package nextstep.subway.path.domain.fare;

import java.util.Objects;

public class Fare implements FareRule {
    private static final int BASE_FARE = 1250;
    private static final int EXTRA_FARE = 100;

    private final DistancePolicy distancePolicy = new DistancePolicy();
    private final DiscountPolicy discountPolicy = new DiscountPolicy();

    private final int fare;

    public Fare() {
        this.fare = BASE_FARE;
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    @Override
    public Fare extraFare(int distance, int lineFare) {
        int overDistance = distancePolicy.getOverDistance(distance);
        int distanceFare = BASE_FARE + (overDistance * EXTRA_FARE);
        return new Fare(distanceFare + lineFare);
    }

    @Override
    public Fare discount(int age) {
        int discountFare = discountPolicy.getDiscountFare(age, this);
        return new Fare(discountFare);
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
