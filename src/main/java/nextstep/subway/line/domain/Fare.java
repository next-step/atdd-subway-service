package nextstep.subway.line.domain;


public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int DEFAULT_EXTRA_FARE = 0;
    private static final int STANDARD_LONG_DISTANCE = 50;
    private static final int STANDARD_SHORT_DISTANCE = 10;

    private int defaultFare;
    private int distance;
    private int extraFare;

    public Fare(int distance) {
        this(distance, DEFAULT_EXTRA_FARE);
    }

    public Fare(int distance, int extraFare) {
        this(distance, extraFare, DEFAULT_FARE);
    }

    public Fare(int distance, int extraFare, int defaultFare) {
        this.distance = distance;
        this.extraFare = extraFare;
        this.defaultFare = defaultFare;
    }


    public int getFare() {
        defaultFare += calculateOverFare();
        defaultFare += extraFare;

        return defaultFare;
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
