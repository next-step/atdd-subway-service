package nextstep.subway.fare.application;

public class FareCalculatorService {
    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1_250;
    private static final int MAX_DISTANCE = 50;

    private static final int FIVE_KILOMETRE = 5;
    private static final int EIGHT_KILOMETRE = 8;

    public static int calculate(int distance) {
        if (DEFAULT_DISTANCE >= distance) {
            return DEFAULT_FARE;
        }

        if (MAX_DISTANCE >= distance) {
            return DEFAULT_FARE + calculateOverFareAsFiveKm(distance - DEFAULT_DISTANCE);
        }

        int calculationFareAsFiveKm = calculateOverFareAsFiveKm(MAX_DISTANCE - DEFAULT_DISTANCE);
        int maxCalculateFare = calculateOverFareAsEightKm(distance - MAX_DISTANCE) + calculationFareAsFiveKm;
        return DEFAULT_FARE + maxCalculateFare;
    }

    private static int calculateOverFareAsFiveKm(int distance) {
        return (int) ((Math.ceil((distance - 1) / FIVE_KILOMETRE) + 1) * 100);
    }

    private static int calculateOverFareAsEightKm(int distance) {
        return (int) ((Math.ceil((distance - 1) / EIGHT_KILOMETRE) + 1) * 100);
    }

}
