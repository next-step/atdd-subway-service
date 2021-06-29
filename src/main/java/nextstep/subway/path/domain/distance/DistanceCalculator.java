package nextstep.subway.path.domain.distance;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.domain.Calculator;
import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.path.domain.ShortestDistance;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DistanceCalculator implements Calculator {
    private static final List<DistancePremiumPolicy> distancePremiumPolicies = Collections.unmodifiableList(
            Arrays.asList(
                    new DefaultDistancePremiumPolicy(),
                    new MidRangeDistancePremiumPolicy(),
                    new LongRangeDistancePremiumPolicy()
            )
    );


    @Override
    public Money calc(Money money, LoginMember loginMember, ShortestDistance shortestDistance) {
        for (DistancePremiumPolicy premiumPolicy : distancePremiumPolicies) {
            money = calcFareIfSupported(premiumPolicy, shortestDistance.shortestDistance(), money);
        }

        return money;
    }

    private Money calcFareIfSupported(DistancePremiumPolicy premiumPolicy, Distance distance, Money money) {
        if (premiumPolicy.isSupport(distance)) {
            money = premiumPolicy.calcFare(distance, money);
        }

        return money;
    }
}
