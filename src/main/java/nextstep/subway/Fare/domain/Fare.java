package nextstep.subway.Fare.domain;

public class Fare {
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

    public Fare calculatorDiscount(Age age) {
        return new Fare((int) ((value - age.getDiscountFare()) * age.getMultipleRate()));
    }
}
