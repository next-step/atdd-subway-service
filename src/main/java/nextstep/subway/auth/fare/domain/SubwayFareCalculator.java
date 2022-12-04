package nextstep.subway.auth.fare.domain;

import nextstep.subway.common.exception.ErrorEnum;

public class SubwayFareCalculator {
    public static final int ZERO_DISTANCE = 0;
    public static final int BASIC_FARE = 1_250;
    public static final int UNIT_5_KM = 5;
    public static final int UNIT_8_KM = 8;
    public static final int SURCHARGE_FARE = 100;
    public static final int MAX_DISTANCE_OF_BASIC_FARE = 10;
    public static final int FIRST_SECTION_DISTANCE = 10;
    public static final int SECOND_SECTION_DISTANCE = 50;

    public static int calculate(int distance) {
        checkInvalidDistance(distance);
        int fare = BASIC_FARE;

        if (distance <= MAX_DISTANCE_OF_BASIC_FARE) {
            return BASIC_FARE;
        }

        if (distance >= FIRST_SECTION_DISTANCE && distance <= SECOND_SECTION_DISTANCE) {
            fare += calculateOverFare(distance, FIRST_SECTION_DISTANCE, UNIT_5_KM);
        }

        if (distance > SECOND_SECTION_DISTANCE) {
             fare += calculateOverFare(distance, SECOND_SECTION_DISTANCE, UNIT_8_KM);
        }

        return fare;
    }

    private static void checkInvalidDistance(int distance) {
        if (distance <= ZERO_DISTANCE) {
            throw new IllegalArgumentException(ErrorEnum.DISTANCE_GREATER_ZERO.message());
        }
    }

    private static int calculateOverFare(int distance, int minDistance, int unitKm) {
        int overDistance = distance - minDistance;
        return (int) ((Math.ceil((overDistance - 1) / unitKm) + 1) * SURCHARGE_FARE);
    }
}
