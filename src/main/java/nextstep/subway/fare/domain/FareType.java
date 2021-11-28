package nextstep.subway.fare.domain;

public enum FareType {
    BASIC(1250),
    ADDITIONAL(100);

    private final int fare;

    FareType(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }
}
