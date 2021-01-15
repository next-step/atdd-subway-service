package nextstep.subway.fare.domain;

public class Fare {

    private static final int BASIC_DISTANCE = 10;
    private static final int OVER_DISTANCE = 50;
    private static final int BASIC_FARE = 1_250;
    public static final int OVER_FARE_STANDARD_1 = 5;
    public static final int OVER_FARE_STANDARD_2 = 8;

    private int fare;

    public void calculateFare(int distance) {
        this.fare = calculateStandardFare(distance);
    }

    private int calculateStandardFare(int distance) {
        if (distance <= BASIC_DISTANCE) {
            return BASIC_FARE;
        }

        if (distance <= OVER_DISTANCE) {
            return BASIC_FARE + calculateOverFare(distance - BASIC_DISTANCE, OVER_FARE_STANDARD_1);
        }

        return BASIC_FARE + calculateOverFare(OVER_DISTANCE - BASIC_DISTANCE, OVER_FARE_STANDARD_1) +
                calculateOverFare(distance - OVER_DISTANCE, OVER_FARE_STANDARD_2);
    }

    // 5km 마다 100원 추가 로직
    private int calculateOverFare(int distance, int eachStandard) {
        return (int) ((Math.ceil((distance - 1) / eachStandard) + 1) * 100);
    }

    public int getFare() {
        return this.fare;
    }
}
