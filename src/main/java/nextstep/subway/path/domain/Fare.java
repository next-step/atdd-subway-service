package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    public static Fare plusFareFromDefaultFare(int additionalFare) {
        return new Fare(DEFAULT_FARE + additionalFare);
    }

    public Fare plus(Fare otherFare) {
        return new Fare(this.fare + otherFare.getFare());
    }

    public Fare plusDefaultFare() {
        return new Fare(this.fare + DEFAULT_FARE);
    }

    public Fare deduct(int deductFare, DiscountPolicy discountPolicy) {
        BigDecimal rate = BigDecimal.valueOf(100 - discountPolicy.getRate()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        return new Fare(BigDecimal.valueOf(this.fare - deductFare).multiply(rate).intValue());
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
