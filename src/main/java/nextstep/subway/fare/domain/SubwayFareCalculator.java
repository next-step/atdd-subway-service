package nextstep.subway.fare.domain;

public class SubwayFareCalculator {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int MAX_LIMIT_DISTANCE = 50;

    public static final int DEFAULT_FARE = 1_250;
    private static final int EXTRA_FARE = 100;

    private static final int FIVE_KM = 5;
    private static final int EIGHT_KM = 8;

    public int getDefaultFare() {
        return DEFAULT_FARE;
    }

    public static int calculate(int distance) {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }

        if (distance <= MAX_LIMIT_DISTANCE) {
            return DEFAULT_FARE + calculateOverPrice(distance - DEFAULT_DISTANCE, FIVE_KM);
        }

        int overPrice = calculateOverPrice(MAX_LIMIT_DISTANCE - DEFAULT_DISTANCE, FIVE_KM);
        overPrice += calculateOverPrice(distance - MAX_LIMIT_DISTANCE, EIGHT_KM);
        return DEFAULT_FARE + overPrice;
    }

    public static int calculateOverPrice(int overDistance, int chargeKm) {
        return (int) ((Math.ceil((overDistance - 1) / chargeKm) + 1) * EXTRA_FARE);
    }
}
