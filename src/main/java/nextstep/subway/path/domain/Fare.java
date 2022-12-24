package nextstep.subway.path.domain;

public class Fare {

    private static final long INIT_FARE = 1_250;

    private long fare;

    private Fare() {
        this.fare = INIT_FARE;
    }

    public static Fare from() {
        return new Fare();
    }

    public long currentFare() {
        return this.fare;
    }
}
