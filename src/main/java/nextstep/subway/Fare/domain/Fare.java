package nextstep.subway.Fare.domain;

public class Fare {
    private static final int DISCOUNT_FARE = 350;
    private int value;

    public Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Fare plus(Fare fare) {
        return new Fare(value + fare.getValue());
    }

    public Fare discountChild() {
        return new Fare((int) ((value - DISCOUNT_FARE) * 0.5));
    }

    public Fare discountTeenager() {
        return new Fare((int) ((value - DISCOUNT_FARE) * 0.8));
    }
}
