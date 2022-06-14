package nextstep.subway.fare.domain;

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
}
