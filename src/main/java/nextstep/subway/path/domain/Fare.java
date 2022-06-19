package nextstep.subway.path.domain;

public class Fare {
    private int value;

    private Fare(Path path) {
        this.value = calculateOverFare(path.getDistance());
    }

    private int calculateOverFare(int distance) {
        FareDistanceType distanceType = FareDistanceType.typeOf(distance);
        return distanceType.calculateFare(distance);
    }

    public static Fare from(Path path) {
        return new Fare(path);
    }

    public int getValue() {
        return value;
    }
}
