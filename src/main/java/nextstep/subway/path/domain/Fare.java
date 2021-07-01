package nextstep.subway.path.domain;

public class Fare {
    private static final int BASIC_FARE = 1250;

    private final int fare;

    public Fare() {
        this.fare = BASIC_FARE;
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFareValue() {
        return fare;
    }
}
