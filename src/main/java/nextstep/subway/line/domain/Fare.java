package nextstep.subway.line.domain;

public class Fare {

    private static final long EXTRA_FARE = 100;
    private static final long DEFAULT_FARE = 1_250;
    private static final long DEFAULT_FARE_DISTANCE_LIMIT = 10;
    private static final long SHORT_EXTRA_FARE_DISTANCE_LIMIT = 50;
    private static final long SHORT_EXTRA_FARE_CHARGE_DISTANCE = 5;
    private static final long LONG_EXTRA_FARE_CHARGE_DISTANCE = 8;
    private long fare;

    public Fare(long fare) {
        this.fare = fare;
    }

    public static Fare calculateFare(long distance) {
        if (distance <= DEFAULT_FARE_DISTANCE_LIMIT) {
            return new Fare(DEFAULT_FARE);
        } else if (distance <= SHORT_EXTRA_FARE_DISTANCE_LIMIT) {
            return new Fare(shortExtraDistanceCalculate(distance) + DEFAULT_FARE);
        } else {
            return new Fare(longExtraDistanceCalculate(distance) + DEFAULT_FARE);
        }
    }

    public static long shortExtraDistanceCalculate(long distance) {
        return extraDistanceCalculate(distance - DEFAULT_FARE_DISTANCE_LIMIT, SHORT_EXTRA_FARE_CHARGE_DISTANCE) * EXTRA_FARE;
    }

    public static long longExtraDistanceCalculate(long distance) {
        return shortExtraDistanceCalculate(SHORT_EXTRA_FARE_DISTANCE_LIMIT) + extraDistanceCalculate(distance - SHORT_EXTRA_FARE_DISTANCE_LIMIT, LONG_EXTRA_FARE_CHARGE_DISTANCE) * EXTRA_FARE;
    }

    public static long extraDistanceCalculate(long distance, long extraChargeDistance) {

        if (distance % extraChargeDistance == 0) {
            return distance / extraChargeDistance;
        }
        return distance / extraChargeDistance + 1;
    }

    public long value() {
        return fare;
    }
}
