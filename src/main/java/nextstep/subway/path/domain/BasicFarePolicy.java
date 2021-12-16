package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

public class BasicFarePolicy implements FarePolicy {
    private static final int BASE_FARE = 1250;

    @Override
    public Fare calculateFare(Distance distance) {
        return Fare.from(BASE_FARE);
    }
}
