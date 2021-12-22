package nextstep.subway.path.domain;

public class Fare {
    private int fare;

    public Fare() {

    }

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
