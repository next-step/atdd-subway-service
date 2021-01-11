package nextstep.subway.path.domain.calcurator;

import java.util.Arrays;
import nextstep.subway.path.domain.Money;

/**
 * @author : leesangbae
 * @project : subway
 * @since : 2021-01-10
 */
public enum DistanceAmount {

    DISTANCE_10_TO_50(10,   50, 5, Money.won(100L)),
    DISTANCE_50_OVER (50, Integer.MAX_VALUE, 8, Money.won(100L)),
    NONE(0, 10, Integer.MAX_VALUE, Money.ZERO);

    private Integer minDistance;
    private Integer maxDistance;
    private Integer distanceStep;
    private Money additionalAmount;

    DistanceAmount(Integer minDistance, Integer maxDistance, Integer distanceStep, Money additionalAmount) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.distanceStep = distanceStep;
        this.additionalAmount = additionalAmount;
    }

    public static Money calculateAdditionalAmount(int distance) {
        DistanceAmount distanceAmount = Arrays.stream(values())
                .filter(it -> it.minDistance <= distance && distance <= it.maxDistance)
                .findFirst()
                .orElse(NONE);

        if (distanceAmount == NONE) {
            return Money.ZERO;
        }
        return distanceAmount
                .getAdditionalAmount()
                .times(Math.ceil((distance - 1) / distanceAmount.getDistanceStep()) + 1);
    }

    public Integer getDistanceStep() {
        return distanceStep;
    }

    public Money getAdditionalAmount() {
        return additionalAmount;
    }
}
