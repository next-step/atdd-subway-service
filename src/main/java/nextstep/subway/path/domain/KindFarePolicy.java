package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

@FunctionalInterface
public interface KindFarePolicy {
    Fare calculateFare(Fare fare) ;
}
