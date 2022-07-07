package nextstep.subway.fare;

import java.util.Objects;

public class Fare {
    private static final int BASIC_FARE = 1250;
    private int value;

    public static Fare basic() {
        return new Fare(BASIC_FARE);
    }

    public Fare() {
    }

    public Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static Fare create5kmExtraCharge(Distance extraDistance) {
        return new Fare(BASIC_FARE + (int) ((Math.ceil((extraDistance.getDistance() - 1) / 5) + 1) * 100));
    }

    public static Fare create8kmExtraCharge(Fare additionalCharge5KM, Distance extraDistance) {
        return new Fare(additionalCharge5KM.getValue() + (int) ((Math.ceil((extraDistance.getDistance() - 1) / 8) + 1) * 100));
    }

    public Fare add(Fare lineFare) {
        return new Fare(this.value + lineFare.getValue());
    }

    public Fare minus(Fare target) {
        if (value < target.value) {
            throw new IllegalArgumentException();
        }
        return new Fare(value - target.value);
    }

    public Fare discountPercent(int discountRate) {
        return new Fare((int) (value - (value * discountRate * 0.01)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
