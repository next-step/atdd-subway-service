package nextstep.subway.path.domain.distance;

import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

public class LongRangeDistancePremiumPolicy implements DistancePremiumPolicy {
    private static final Distance REQUIRED_DISTANCE = new Distance(50);
    @Override
    public Money calcFare(Distance distance, Money money) {
        return null;
    }

    @Override
    public boolean isSupport(Distance distance) {
        return REQUIRED_DISTANCE.compareTo(distance) == -1;
    }
}
