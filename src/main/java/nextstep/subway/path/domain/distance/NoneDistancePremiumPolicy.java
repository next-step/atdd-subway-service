package nextstep.subway.path.domain.distance;

import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

public class NoneDistancePremiumPolicy implements DistancePremiumPolicy {
    @Override
    public Money calcFare(Distance distance, Money money) {
        return DistancePremiumPolicy.DEFAULT_FARE;
    }

    @Override
    public boolean isSupport(Distance distance) {
        return true;
    }
}
