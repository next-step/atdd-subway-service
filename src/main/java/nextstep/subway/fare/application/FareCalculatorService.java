package nextstep.subway.fare.application;

public class FareCalculatorService {
    private static final int DEFAULT_MAXIMUM_DISTANCE = 10;
    private static final int DEFAULT_FARE = 1_250;
    private static final int MAXIMUM_EXTRA_FARE_DISTANCE = 50;

    // 10~50km 이내 : 5km 까지 마다 100원 추가
    private static final int EXTRA_FARE_FIVE_KILOMETRE = 5;

    // 50km 초과 : 8km 까지 마다 100원 추가
    private static final int EXTRA_FARE_EIGHT_KILOMETRE = 8;

    public static int calculate(int distance) {
        if (DEFAULT_MAXIMUM_DISTANCE >= distance) {
            return DEFAULT_FARE;
        }

        if (MAXIMUM_EXTRA_FARE_DISTANCE >= distance) {
            return DEFAULT_FARE + calculateExtraFareAsFiveKm(distance - DEFAULT_MAXIMUM_DISTANCE);
        }

        int calculationFareAsFiveKm = calculateExtraFareAsFiveKm(MAXIMUM_EXTRA_FARE_DISTANCE - DEFAULT_MAXIMUM_DISTANCE);
        int maxCalculateFare = calculateExtraFareAsEightKm(distance - MAXIMUM_EXTRA_FARE_DISTANCE) + calculationFareAsFiveKm;
        return DEFAULT_FARE + maxCalculateFare;
    }

    private static int calculateExtraFareAsFiveKm(int distance) {
        return (int) ((Math.ceil((distance - 1) / EXTRA_FARE_FIVE_KILOMETRE) + 1) * 100);
    }

    private static int calculateExtraFareAsEightKm(int distance) {
        return (int) ((Math.ceil((distance - 1) / EXTRA_FARE_EIGHT_KILOMETRE) + 1) * 100);
    }
}
