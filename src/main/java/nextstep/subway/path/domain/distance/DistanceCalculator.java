package nextstep.subway.path.domain.distance;

import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DistanceCalculator {
    private static final List<DistancePremiumPolicy> distancePremiumPolicies = Collections.unmodifiableList(
            Arrays.asList(
                    new DefaultDistancePremiumPolicy(),
                    new MidRangeDistancePremiumPolicy(),
                    new LongRangeDistancePremiumPolicy()
            )
    );

    public static Money calcDistance(Distance distance) {
        Money money = new Money(0);

        for (DistancePremiumPolicy premiumPolicy : distancePremiumPolicies) {
            money = calcFareIfSupported(premiumPolicy, distance, money);
        }
        return money;
    }

    private static Money calcFareIfSupported(DistancePremiumPolicy premiumPolicy, Distance distance, Money money) {
        if (premiumPolicy.isSupport(distance)) {
            money = premiumPolicy.calcFare(distance, money);
        }
        return money;
    }
}
