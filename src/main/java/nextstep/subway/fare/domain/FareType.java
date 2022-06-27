package nextstep.subway.fare.domain;

public enum FareType {
    BASIC(1250),
    EXTRA(100),
    DEDUCTION(350),
    FREE(0);

    private final int fare;

    FareType(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return this.fare;
    }
}
