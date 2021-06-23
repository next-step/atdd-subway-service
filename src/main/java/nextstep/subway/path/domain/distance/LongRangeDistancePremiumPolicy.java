package nextstep.subway.path.domain.distance;

import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import static java.lang.String.format;

public class LongRangeDistancePremiumPolicy implements DistancePremiumPolicy {
    private static final Distance MINIMUM_DISTANCE = new Distance(50);

    private static final Money PER_8KM_FARE = new Money(100);
    private static final int PER_KM = 8;

    @Override
    public Money calcFare(Distance distance, Money money) {
        if (!isSupport(distance)) {
            throw new IllegalArgumentException(format("%d 초과만 계산이 가능합니다", MINIMUM_DISTANCE.toInt()));
        }

        int distanceOfMoney = calcAvailableDistance(distance);

        return new Money(distanceOfMoney / PER_KM)
                .multi(PER_8KM_FARE)
                .plus(money);
    }

    private int calcAvailableDistance(Distance distance) {
        int distanceOfMoney = distance.toInt();

        return distanceOfMoney - MINIMUM_DISTANCE.toInt();
    }

    @Override
    public boolean isSupport(Distance distance) {
        return MINIMUM_DISTANCE.compareTo(distance) == -1;
    }
}
