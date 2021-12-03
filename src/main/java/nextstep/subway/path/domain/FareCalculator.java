package nextstep.subway.path.domain;

public class FareCalculator {

    private final Path path;

    public FareCalculator(Path path) {
        this.path = path;
    }

    public int calculate() {
        return fareByDistance(path.getDistance()) +
                path.getMaxAddFare();
    }

    private int fareByDistance(int distance) {
        return DistancePolicy.BASE_FARE +
                DistancePolicy.MEDIUM.calculateFare(distance) +
                DistancePolicy.LONG.calculateFare(distance);
    }
}
