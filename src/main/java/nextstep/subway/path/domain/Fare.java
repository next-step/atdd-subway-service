package nextstep.subway.path.domain;

public class Fare {

    private int value;

    private Fare(int value) {
        this.value = value;
    }

    public static Fare from(int value) {
        return new Fare(value);
    }

    public static Fare empty() {
        return new Fare(0);
    }

    public int getValue() {
        return value;
    }

    public Fare add(Fare fare) {
        return new Fare(this.value + fare.value);
    }
}
