package nextstep.subway.fare.domain;

import java.util.Objects;

public class Fare {
    private static final int basicFare = 1250;

    private int fare;
    private final DiscountPolicy discountPolicy;

    private Fare(int fare, DiscountPolicy discountPolicy) {
        this.fare = fare;
        this.discountPolicy = discountPolicy;
    }

    public static Fare of(DiscountPolicy discountPolicy) {
        return new Fare(Fare.basicFare, discountPolicy);
    }

    public static Fare of(int fare, DiscountPolicy discountPolicy) {
        return new Fare(fare, discountPolicy);
    }

    public Fare plus(int fare) {
        return Fare.of(this.fare + fare, this.discountPolicy);
    }

    public int discount() {
        return this.discountPolicy.getDiscount(this.fare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare && discountPolicy == fare1.discountPolicy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare, discountPolicy);
    }
}
