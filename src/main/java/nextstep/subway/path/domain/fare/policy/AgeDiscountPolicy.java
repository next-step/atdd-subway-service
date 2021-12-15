package nextstep.subway.path.domain.fare.policy;

import nextstep.subway.path.domain.fare.Fare;

public interface AgeDiscountPolicy {

    public Fare calculateFare(final Fare fare);
}
