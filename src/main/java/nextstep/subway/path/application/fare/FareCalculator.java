package nextstep.subway.path.application.fare;

public class FareCalculator {

    private static final int BASIC_FARE = 1250;

    public static int calculateFare(int distance) {
        return BASIC_FARE + DistanceFarePolicy.valueOf(distance)
                .calculateOverFare(distance);
    }
}
