package nextstep.subway.fare.policy;

import static nextstep.subway.fare.domain.Fare.BASIC_FARE;

import nextstep.subway.fare.domain.Fare;

public class BasicFare implements FarePolicy {
    @Override
    public Fare calculateFare() {
        return Fare.from(BASIC_FARE);
    }
}
