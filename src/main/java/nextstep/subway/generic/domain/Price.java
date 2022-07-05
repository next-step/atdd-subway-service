package nextstep.subway.generic.domain;

public class Price {
    private static final Distance KM_10 = Distance.valueOf(10);
    private static final Distance KM_50 = Distance.valueOf(50);
    public static final int DEFAULT_PRICE = 1250;
    private final int value;

    protected Price(int value) {
        this.value = value;
    }

    protected Price(Distance distance) {
        this.value = calculatePrice(distance);
    }

    public static Price valueOf(Integer value) {
        return new Price(value);
    }

    public static Price of(Distance distance) {
        return new Price(distance);
    }

    private int calculatePrice(Distance distance) {
        if (distance.isLessThanOrEqualsTo(KM_10)) {
            return DEFAULT_PRICE;
        }

        if (distance.isLessThanOrEqualsTo(KM_50)) {
            return DEFAULT_PRICE + calculateOverFare5(distance.minus(KM_10).getValue());
        }

        return DEFAULT_PRICE + calculateOverFare5(distance.minus(KM_10).getValue()) + calculateOverFare8(
                distance.minus(KM_50).getValue());
    }

    private int calculateOverFare5(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateOverFare8(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }

    public int getValue() {
        return value;
    }

    public Price plus(Price price) {
        return new Price(value + price.value);
    }

    public Price discount(double discountRate) {
        double discountValue = (value - 350) * (1 - discountRate) + 350;
        return new Price((int) discountValue);
    }
}
