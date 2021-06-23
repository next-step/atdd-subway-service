package nextstep.subway.line.domain;


public class Fare {

    private static final int DEFAULT_FARE = 1250;
    private static final int STANDARD_LONG_DISTANCE = 50;
    private static final int STANDARD_SHORT_DISTANCE = 10;

    private int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public static Fare of(int distance) {
        return new Fare(distance);
    }

    public int getFare() {
        int fare = DEFAULT_FARE;
        if(distance > STANDARD_LONG_DISTANCE) {
            int overDistance = distance - STANDARD_LONG_DISTANCE;
            fare += calculateOverFareBy8km(overDistance);
            this.distance -= overDistance;
        }

        if(distance > STANDARD_SHORT_DISTANCE) {
            this.distance -= STANDARD_SHORT_DISTANCE;
            fare += calculateOverFare(distance);
        }

        return fare;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateOverFareBy8km(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
