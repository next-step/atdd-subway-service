package nextstep.subway.path.domain;

public class Fare {
    public static final int MIDDLE_DISTANCE = 10;
    public static final int HIGH_DISTANCE = 50;
    public static final int ADDITIONAL_FARE = 100;
    public static final int MIDDLE_SECTOR_CHARGE_DISTANCE = 5;
    public static final int HIGH_SECTOR_CHARGE_DISTANCE = 8;
    private static final int BASIC_FARE = 1_250;

    private int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare from(int distance) {
        return new Fare(calFare(distance));
    }

    public int getFare() {
        return fare;
    }

    private static int calFare(int distance) {
        return BASIC_FARE + FareDistance.calAdditionalFare(distance);
    }
}
