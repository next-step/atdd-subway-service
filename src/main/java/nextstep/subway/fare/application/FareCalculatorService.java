package nextstep.subway.fare.application;

public class FareCalculatorService {
    private static final int DEFAULT_MAXIMUM_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1_250;
    private static final int MAXIMUM_EXTRA_FARE_DISTANCE = 50;

    private static final int FIVE_KILOMETRE = 5;
    private static final int EIGHT_KILOMETRE = 8;

    public static int calculate(int distance) {
        if (DEFAULT_MAXIMUM_DISTANCE >= distance) {
            return DEFAULT_FARE;
        }

        if (MAXIMUM_EXTRA_FARE_DISTANCE >= distance) {
            return DEFAULT_FARE + calculateOverFareAsFiveKm(distance - DEFAULT_MAXIMUM_DISTANCE);
        }

        int calculationFareAsFiveKm = calculateOverFareAsFiveKm(MAXIMUM_EXTRA_FARE_DISTANCE - DEFAULT_MAXIMUM_DISTANCE);
        int maxCalculateFare = calculateOverFareAsEightKm(distance - MAXIMUM_EXTRA_FARE_DISTANCE) + calculationFareAsFiveKm;
        return DEFAULT_FARE + maxCalculateFare;
    }

    private static int calculateOverFareAsFiveKm(int distance) {
        return (int) ((Math.ceil((distance - 1) / FIVE_KILOMETRE) + 1) * 100);
    }

    private static int calculateOverFareAsEightKm(int distance) {
        return (int) ((Math.ceil((distance - 1) / EIGHT_KILOMETRE) + 1) * 100);
    }

}
