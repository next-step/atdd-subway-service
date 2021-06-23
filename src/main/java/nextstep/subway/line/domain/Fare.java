package nextstep.subway.line.domain;


public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int STANDARD_LONG_DISTANCE = 50;
    private static final int STANDARD_SHORT_DISTANCE = 10;

    private int distance;
    private int extraFare;

    public Fare(int distance) {
        this.distance = distance;
    }

    public Fare(int distance, int extraFare) {
        this.distance = distance;
        this.extraFare = extraFare;
    }

    public int getFare() {
        int fare = DEFAULT_FARE;

        fare += calculateOverFare();
        fare += extraFare;

        return fare;
    }

    private int calculateOverFare() {
        int fare = 0;
        if (distance > STANDARD_LONG_DISTANCE) {
            int overDistance = distance - STANDARD_LONG_DISTANCE;
            fare += calculateOverFare(overDistance, 8);
            this.distance -= overDistance;
        }

        if (distance > STANDARD_SHORT_DISTANCE) {
            this.distance -= STANDARD_SHORT_DISTANCE;
            fare += calculateOverFare(distance, 5);
        }
        return fare;
    }

    private int calculateOverFare(int distance, int distanceUnit) {
        return (int) ((Math.ceil((distance - 1) / distanceUnit) + 1) * 100);
    }
}
