package nextstep.subway.path.domain;

import nextstep.subway.path.application.FarePolicy;

public class BasicFarePolicy implements FarePolicy {
    private static final int BASE_FARE = 1250;

    @Override
    public Fare calculateFare(int distance) {
        return Fare.from(BASE_FARE);
    }
}
