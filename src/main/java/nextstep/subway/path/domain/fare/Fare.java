package nextstep.subway.path.domain.fare;

public class Fare extends FareRule {

    private final int fare;

    public Fare(int distance) {
        this.fare = distanceFare(distance);
    }

    public Fare(int distance, int lineFare) {
        int distanceFare = distanceFare(distance);
        this.fare = distanceFare + lineFare;
    }

    public int getFare() {
        return fare;
    }
}
