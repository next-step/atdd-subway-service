package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

public interface DistancePremiumPolicy {
    Money calcFare(Distance distance, Money money);
}
