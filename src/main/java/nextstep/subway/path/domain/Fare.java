package nextstep.subway.path.domain;

public class Fare {

    private static final int DEFAULT_FARE = 1250;

    private final int value;

    public Fare() {
        this.value = DEFAULT_FARE;
    }

    public Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
