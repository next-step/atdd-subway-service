package nextstep.subway.path.domain;

public class Fare {

    public static final Fare DEFAULT_FARE = new Fare(1250);

    private final int value;

    public Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
