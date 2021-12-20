package nextstep.subway.fare;

public class FarePolicy {
    private static final int BASE_FARE = 1_250;

    public static int calculateDistanceOverFare(int distance) {
        return BASE_FARE + DistanceFarePolicy.calculateOverFare(distance);
    }
}
