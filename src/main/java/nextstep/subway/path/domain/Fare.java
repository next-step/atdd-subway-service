package nextstep.subway.path.domain;

public class Fare {
    private final int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    public Fare addFare(int fare) {
        return new Fare(this.fare + fare);
    }
}
