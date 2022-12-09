package nextstep.subway.path.domain;

public class Fare {

    public static final int DEFAULT_FARE = 1250;
    public static final int OVER_FARE_FIRST_APPLY_DISTANCE = 10;
    public static final int OVER_FARE_SECOND_APPLY_DISTANCE = 50;
    public static final int OVER_FARE = 100;
    public static final int OVER_FARE_FIRST_PER_RANGE = 5;
    public static final int OVER_FARE_SECOND_PER_RANGE = 8;
    private final int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        if (distance <= OVER_FARE_FIRST_APPLY_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (this.distance <= OVER_FARE_SECOND_APPLY_DISTANCE) {
            return DEFAULT_FARE + calculateOverFareByFirstRange(this.distance - OVER_FARE_FIRST_APPLY_DISTANCE);
        }
        return DEFAULT_FARE + calculateOverFareByFirstRange(OVER_FARE_SECOND_APPLY_DISTANCE - OVER_FARE_FIRST_APPLY_DISTANCE) + calculateOverFareBySecondRange(this.distance - OVER_FARE_SECOND_APPLY_DISTANCE);
    }

    private int calculateOverFareByFirstRange(int distance) {
        return (int) ((Math.ceil((distance - 1) / OVER_FARE_FIRST_PER_RANGE) + 1) * OVER_FARE);
    }

    private int calculateOverFareBySecondRange(int distance) {
        return (int) ((Math.ceil((distance - 1) / OVER_FARE_SECOND_PER_RANGE) + 1) * OVER_FARE);
    }
}
