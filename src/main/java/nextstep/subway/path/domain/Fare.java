package nextstep.subway.path.domain;

public class Fare {

    private static final long ZERO_FARE = 0L;
    private static final long INIT_FARE = 1_250L;
    private static final int FIRST_FARE_SECTION_DELIMITER = 10;
    private static final int SECOND_FARE_SECTION_DELIMITER = 50;
    private static final int ADD_FARE = 100;
    private static final int FIRST_FARE_SECTION_PER_DISTANCE = 5;
    private static final int SECOND_FARE_SECTION_PER_DISTANCE = 8;

    private long fare;

    private Fare() {
        this.fare = INIT_FARE;
    }

    private Fare(long addFare) {
        this.fare = INIT_FARE + addFare;
    }

    public static Fare from() {
        return new Fare();
    }

    public static Fare from(long addFare) {
        return new Fare(addFare);
    }

    public long currentFare() {
        return this.fare;
    }

    public void calculateFareByDistanceProportional(int distance) {
        long tempFare = ZERO_FARE;
        if (isBelongToFirstFareSection(distance)) {
            tempFare += calculateOverFareWhenFirstFareSection(distance - FIRST_FARE_SECTION_DELIMITER);
        }
        if (isBelongToSecondFareSection(distance)) {
            tempFare += calculateOverFareWhenSecondFareSection(distance - SECOND_FARE_SECTION_DELIMITER);
        }
        this.fare += tempFare;
    }

    private boolean isBelongToSecondFareSection(int distance) {
        return distance > SECOND_FARE_SECTION_DELIMITER;
    }

    private boolean isBelongToFirstFareSection(int distance) {
        return distance > FIRST_FARE_SECTION_DELIMITER;
    }

    private long calculateOverFareWhenFirstFareSection(int distance) {
        if (isBelongToSecondFareSection(distance + FIRST_FARE_SECTION_DELIMITER)) {
            distance = SECOND_FARE_SECTION_DELIMITER - FIRST_FARE_SECTION_DELIMITER;
        }
        return (int) ((Math.ceil((distance - 1) / FIRST_FARE_SECTION_PER_DISTANCE) + 1) * ADD_FARE);
    }

    private long calculateOverFareWhenSecondFareSection(int distance) {
        return (int) ((Math.ceil((distance - 1) / SECOND_FARE_SECTION_PER_DISTANCE) + 1) * ADD_FARE);
    }
}
