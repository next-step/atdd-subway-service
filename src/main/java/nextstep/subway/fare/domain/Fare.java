package nextstep.subway.fare.domain;

public enum Fare {

    BASIC(10, 5),
    ADDITIONAL(50, 8);

    private static final int DEFAULT_CHARGE = 1250;
    private static final int ADDITIONAL_CHARGE = 100;
    private final int distance;
    private final int criteria;

    Fare(int distance, int criteria) {
        this.distance = distance;
        this.criteria = criteria;
    }

    public int getDistance() {
        return distance;
    }

    public int getCriteria() {
        return criteria;
    }

    public static int calculateFare(int distance) {
        int fare = DEFAULT_CHARGE;

        if (distance > ADDITIONAL.distance) {
            fare += calculateOverFare(distance - ADDITIONAL.distance, ADDITIONAL.criteria);
            distance = ADDITIONAL.distance;
        }
        if (distance > BASIC.distance) {
            fare += calculateOverFare(distance - BASIC.distance, BASIC.criteria);
        }
        return fare;
    }

    private static int calculateOverFare(int distance, int criteria) {
        return (int) ((Math.ceil((distance - 1) / criteria) + 1) * ADDITIONAL_CHARGE);
    }
}
