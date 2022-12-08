package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {

    public static final int BASIC_FARE = 1250;
    public static final int ADDITIONAL_FARE_UNIT = 100;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL1 = 10;
    public static final int ADDITIONAL_FARE_DISTANCE_LEVEL2 = 50;
    public static final int DEDUCTION_AMOUNT = 350;
    public static final double DISCOUNT_RATE_CHILD = 0.5;
    public static final double DISCOUNT_RATE_TEEN = 0.2;

    private int value;

    public Fare(final int fare) {
        this.value = fare;
    }

    public void applyDiscountFare(final double rate) {
        value = (int) (value - ((value - DEDUCTION_AMOUNT) * rate));
    }

    public int value() {
        return value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
