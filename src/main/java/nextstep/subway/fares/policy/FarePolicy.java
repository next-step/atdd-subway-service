package nextstep.subway.fares.policy;

import nextstep.subway.fares.domain.Fare;

public interface FarePolicy {
    void calculateFare(Fare fare, FareContext fareContext);
}
