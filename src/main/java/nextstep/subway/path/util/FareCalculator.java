package nextstep.subway.path.util;

public class FareCalculator {
    private static final int BASE_FARE = 1250;
    private static final int BASE_DISTANCE = 10;

    private FareCalculator() {
    }

    public static int calculateFare(int distance) {
        int overDistance = distance - BASE_DISTANCE;

        if (overDistance > 0) {
            return BASE_FARE + calculateOverFare(overDistance);
        }
        return BASE_FARE;
    }

    private static int calculateOverFare(int distance) {
        if (distance > 50) {
            return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
        }
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
