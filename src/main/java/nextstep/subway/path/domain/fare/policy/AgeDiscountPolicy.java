package nextstep.subway.path.domain.fare.policy;

import nextstep.subway.path.domain.fare.Fare;

public interface AgeDiscountPolicy {

    Fare calculateFare(final Fare fare);
}
