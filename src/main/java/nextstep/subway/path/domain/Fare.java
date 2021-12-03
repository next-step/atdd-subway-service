package nextstep.subway.path.domain;

public class Fare {

    public static final Fare DEFAULT_FARE = new Fare(1250);

    private final int value;

    public static Fare extra(int lineFare, int distance) {
        int distanceFare = calculateOverFare(distance);
        return DEFAULT_FARE.plus(lineFare + distanceFare);
    }

    private static int calculateOverFare(int distance) {
        int fare = 0;
        if (distance > 10 && distance <= 50) {
            fare += (int) ((Math.ceil(((distance - 10) - 1) / 5) + 1) * 100);
        }
        if (distance > 50) {
            fare += (int) ((Math.ceil(((distance - 50) - 1) / 8) + 1) * 100) + 800;
        }
        return fare;
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
