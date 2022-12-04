package nextstep.subway.fare.domain;

import nextstep.subway.common.exception.ErrorEnum;

public class SubwayFareCalculator {
    public static final int ZERO_DISTANCE = 0;
    public static final int BASIC_FARE = 1_250;
    public static final int SURCHARGE_FARE = 100;
    public static final int UNIT_5_KM = 5;
    public static final int UNIT_8_KM = 8;
    public static final int MAX_DISTANCE_OF_BASIC_FARE = 10;
    public static final int FIRST_SECTION_DISTANCE = 10;
    public static final int SECOND_SECTION_DISTANCE = 50;

    private static int lineFare;
    private static int age = 19;

    private static final int MIN_LINE_FARE = 0;
    private static final int MIN_ADULT_AGE = 19;

    public SubwayFareCalculator() {
        this.lineFare = MIN_LINE_FARE;
        this.age = MIN_ADULT_AGE;
    }

    private SubwayFareCalculator(int lineFare, int age) {
        this.lineFare = lineFare;
        this.age = age;
    }

    public static SubwayFareCalculator of(int lineFare, int age) {
        return new SubwayFareCalculator(lineFare, age);
    }

    public static int calculate(int distance) {
        checkInvalidDistance(distance);
        int fare = calculateWithDistance(distance);
        fare += lineFare;

        AgeFarePolicy policy = AgeFarePolicy.findByAge(age);
        return policy.discount(fare);
    }

    private static int calculateWithDistance(int distance) {
        int fare = BASIC_FARE;

        if (distance <= MAX_DISTANCE_OF_BASIC_FARE) {
            return BASIC_FARE;
        }

        fare += calculateOverFare(distance, FIRST_SECTION_DISTANCE, UNIT_5_KM);

        if (isOverFirstSection(distance)) {
            fare += calculateOverFare(distance, SECOND_SECTION_DISTANCE + MAX_DISTANCE_OF_BASIC_FARE, UNIT_8_KM);
        }

        return fare;
    }

    private static void checkInvalidDistance(int distance) {
        if (distance <= ZERO_DISTANCE) {
            throw new IllegalArgumentException(ErrorEnum.DISTANCE_GREATER_ZERO.message());
        }
    }

    private static boolean isOverFirstSection(int distance) {
        return distance > SECOND_SECTION_DISTANCE + MAX_DISTANCE_OF_BASIC_FARE;
    }

    private static int calculateOverFare(int distance, int minDistance, int unitKm) {
        int overDistance = (distance - minDistance >= 50) ? 50 : distance - minDistance;
        return (int) ((Math.ceil((overDistance - 1) / unitKm) + 1) * SURCHARGE_FARE);
    }
}
