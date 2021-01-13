package nextstep.subway.path.domain;

public class Fare {
    private static final long INITIAL_FEE = 1250;

    private long fare;

    public Fare() {
        this.fare = INITIAL_FEE;
    }

    public long getFare() {
        return fare;
    }

    public void addFare(long fare) {
        this.fare += fare;
    }
    public void minusFare(long fare) {
        this.fare -= fare;
    }

}
