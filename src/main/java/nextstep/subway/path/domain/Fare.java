package nextstep.subway.path.domain;

public class Fare {

    public static final Fare DEFAULT_FARE = new Fare(1250);

    private final int value;

    public static Fare of(int lineFare) {
        return DEFAULT_FARE.plus(lineFare);
    }

    private Fare plus(int lineFare) {
        return new Fare(this.value + lineFare);
    }

    public Fare(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
