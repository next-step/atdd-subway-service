package nextstep.subway.fare;

public class FarePolicy {
    public static int calculateDistanceOverFare(int distance) {
        return DistanceFarePolicy.calculateOverFare(distance);
    }
}
