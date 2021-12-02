package nextstep.subway.path.domain;

public class FareCalculator {

    private final int distance;

    public FareCalculator(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        int fare = DistancePolicy.BASE_FARE;
        fare += DistancePolicy.MEDIUM.calculateFare(distance);
        fare += DistancePolicy.LONG.calculateFare(distance);
        return fare;
    }
}
