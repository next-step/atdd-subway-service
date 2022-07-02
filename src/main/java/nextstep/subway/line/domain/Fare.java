package nextstep.subway.line.domain;

public class Fare {

    private final int DEFAULT_FARE = 1_250;
    private final int EXTRA_FARE = 100;
    private final int DEFAULT_FARE_DISTANCE_LIMIT = 10;
    private final int SHORT_EXTRA_FARE_DISTANCE_LIMIT = 50;
    private final int SHORT_EXTRA_FARE_CHARGE_DISTANCE = 5;
    private final int LONG_EXTRA_FARE_CHARGE_DISTANCE = 8;
    private int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public int calculateFare(int distance) {

        if (distance <= DEFAULT_FARE_DISTANCE_LIMIT) {
            return DEFAULT_FARE;
        } else if (distance <= SHORT_EXTRA_FARE_DISTANCE_LIMIT) {
            return shortExtraDistanceCalculate(distance) + DEFAULT_FARE;
        } else {
            return longExtraDistanceCalculate(distance) + DEFAULT_FARE;
        }
    }

    public int shortExtraDistanceCalculate(int distance) {
        return extraDistanceCalculate(distance - DEFAULT_FARE_DISTANCE_LIMIT, SHORT_EXTRA_FARE_CHARGE_DISTANCE) * EXTRA_FARE;
    }

    public int longExtraDistanceCalculate(int distance) {
        return shortExtraDistanceCalculate(SHORT_EXTRA_FARE_DISTANCE_LIMIT) + extraDistanceCalculate(distance - SHORT_EXTRA_FARE_DISTANCE_LIMIT, LONG_EXTRA_FARE_CHARGE_DISTANCE) * EXTRA_FARE;
    }

    public int extraDistanceCalculate(int distance, int extraChargeDistance) {

        if (distance % extraChargeDistance == 0) {
            return distance / extraChargeDistance;
        }
        return distance / extraChargeDistance + 1;
    }
}
