package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

public interface DistancePremiumPolicy {
    public static final Money DEFAULT_FARE = new Money(1250);

    Money calcFare(Distance distance, Money money);

    boolean isSupport(Distance distance);
}
