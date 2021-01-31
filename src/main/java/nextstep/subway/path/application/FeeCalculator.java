package nextstep.subway.path.application;

import nextstep.subway.member.domain.DiscountStrategy;
import nextstep.subway.member.dto.Money;

public class FeeCalculator {

    private static final int DEFAULT_DISTANCE = 10;
    private static final int DEFAULT_FEE = 1250;
    private static final int ADDITIONAL_FEE = 100;
    private static final int MID_DIVIDE_DISTANCE = 5;
    private static final int LONG_DIVIDE_DISTANCE = 8;
    public static final int LONG_DISTANCE = 50;

    public static Money calculate(Money extraCharge, int distance, DiscountStrategy discountStrategy) {
        Money sum = extraCharge.add(calculateByDistance(distance));
        return discountStrategy.discount(sum);
    }

    private static Money calculateByDistance(int distance) {
        Money money = Money.of(DEFAULT_FEE);
        if (distance <= DEFAULT_DISTANCE) {
            return money;
        }
        return money.add(getMidDistanceMoney(distance)).add(getLongDistanceMoney(distance));
    }

    private static Money getMidDistanceMoney(int distance) {
        int midDistance = Math.min(distance, LONG_DISTANCE) - DEFAULT_DISTANCE;
        int times = midDistance / MID_DIVIDE_DISTANCE;
        return Money.of(ADDITIONAL_FEE * times);
    }

    private static Money getLongDistanceMoney(int distance) {
        int maxDistance = Math.max(distance - LONG_DISTANCE, 0);
        int maxTimes = maxDistance / LONG_DIVIDE_DISTANCE;
        return Money.of(ADDITIONAL_FEE * maxTimes);
    }
}
