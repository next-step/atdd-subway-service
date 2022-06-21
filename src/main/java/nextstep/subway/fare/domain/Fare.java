package nextstep.subway.fare.domain;

public enum Fare {

    BASIC(10, 5),
    ADDITIONAL(50, 8);
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

    public boolean isLonger(int distance) {
        return distance > this.distance;
    }

    public int calculateOverFare(int distance) {
        return (int) ((Math.ceil(((distance - this.distance) - 1) / criteria) + 1) * ADDITIONAL_CHARGE);
    }
}
