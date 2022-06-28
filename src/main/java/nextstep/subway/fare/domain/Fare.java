package nextstep.subway.fare.domain;

public class Fare {
    private static final int DEFAULT_FARE = 1_250;

    private final int value;

    public Fare(int value) {
        this.value = value;
    }
}
