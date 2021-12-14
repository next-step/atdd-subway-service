package nextstep.subway.path.domain.fare;

import nextstep.subway.line.domain.Distance;

public class Fare {

    static final int BASE_FARE = 1_250;

    private final int fare;

    private Fare(final int fare) {
        this.fare = fare;
    }

    public static Fare of(final Distance distance) {
        return new Fare(BASE_FARE + OverFarePolicy.calculateOverFare(distance.getDistance()));
    }

    public int getFare() {
        return fare;
    }
}
