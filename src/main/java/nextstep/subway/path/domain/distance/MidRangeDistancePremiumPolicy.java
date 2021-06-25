package nextstep.subway.path.domain.distance;

import nextstep.subway.path.domain.DistancePremiumPolicy;
import nextstep.subway.wrapped.Distance;
import nextstep.subway.wrapped.Money;

import static java.lang.String.format;

public class MidRangeDistancePremiumPolicy implements DistancePremiumPolicy {
    private static final Distance MINIMUM_DISTANCE = new Distance(10);
    private static final Distance MAXIMUM_DISTANCE = new Distance(50);

    private static final Money PER_5KM_FARE = new Money(100);
    private static final Money PER_KM = new Money(5);

    @Override
    public Money calcFare(Distance distance, Money money) {
        if (!isSupport(distance)) {
            throw new IllegalArgumentException(format("%d 초과만 계산이 가능합니다", MINIMUM_DISTANCE.toInt()));
        }

        int distanceOfMoney = calcAvailableDistance(distance);

        return new Money(distanceOfMoney)
                .divide(PER_KM)
                .multi(PER_5KM_FARE)
                .plus(money);
    }

    private int calcAvailableDistance(Distance distance) {
        int distanceOfMoney = distance.toInt();

        if (distanceOfMoney >= MAXIMUM_DISTANCE.toInt()) {
            distanceOfMoney = MAXIMUM_DISTANCE.toInt();
        }

        return distanceOfMoney - MINIMUM_DISTANCE.toInt();
    }

    @Override
    public boolean isSupport(Distance distance) {
        return MINIMUM_DISTANCE.compareTo(distance) == -1;
    }
}
