package nextstep.subway.path.domain;

public class Fare {

    public static final int DEFAULT_FARE = 1250;
    public static final int DEFAULT_DISTANCE = 10;
    private final int distance;

    public Fare(int distance) {
        this.distance = distance;
    }

    public int calculate() {
        if (distance <= DEFAULT_DISTANCE) {
            return DEFAULT_FARE;
        }
        if (10 < this.distance && this.distance <= 50) {
            return DEFAULT_FARE + calculateOverFareByFiveKm(this.distance - DEFAULT_DISTANCE);
        }
        return DEFAULT_FARE + calculateOverFareByFiveKm(50 - DEFAULT_DISTANCE) + calculateOverFareByEightKm(this.distance - 50);
    }

    private int calculateOverFareByFiveKm(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private int calculateOverFareByEightKm(int distance) {
        return (int) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}
