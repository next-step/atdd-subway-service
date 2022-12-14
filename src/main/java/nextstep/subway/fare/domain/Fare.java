package nextstep.subway.fare.domain;

public enum Fare {
    BASIC(1250),
    FREE(0);

    private int fare;

    Fare(int fare) {
        this.fare = fare;
    }
}
