package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.wrapped.Money;

public interface FareCalculator {
    public static final FareCalculator DEFAULT_CALCULATOR = new DefaultFareCalculator();

    Money calcFare(LoginMember loginMember, ShortestDistance shortestDistance);
}
