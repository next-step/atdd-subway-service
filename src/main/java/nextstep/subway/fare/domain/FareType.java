package nextstep.subway.fare.domain;

public enum FareType {
    BASIC(1250),
    ADDITION(100);

    private int fare;

    FareType(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return this.fare;
    }
}
