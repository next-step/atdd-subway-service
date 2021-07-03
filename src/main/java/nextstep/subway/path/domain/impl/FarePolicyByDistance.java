package nextstep.subway.path.domain.impl;

import nextstep.subway.path.domain.FarePolicy;

/**
 * - 기본운임(10㎞ 이내) : 기본운임 1,250원
 * - 이용 거리초과 시 추가운임 부과
 * - 10km초과∼50km까지(5km마다 100원)
 * - 50km초과 시 (8km마다 100원)
 */
public class FarePolicyByDistance implements FarePolicy {
    private final double distance;
    private static final int ADDITIONAL_AMOUNT = 100;
    private static final int FIRST_INCREASING_DISTANCE = 10;
    private static final int SECOND_INCREASING_DISTANCE = 50;
    private static final int FIRST_UNIT = 5;
    private static final int SECOND_UNIT = 8;
    private static final int FIRST_MAX_AMOUNT = (SECOND_INCREASING_DISTANCE - FIRST_INCREASING_DISTANCE) / FIRST_UNIT * ADDITIONAL_AMOUNT;

    public FarePolicyByDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public double calculate(double fare) {
        if (distance > FIRST_INCREASING_DISTANCE && distance <= SECOND_INCREASING_DISTANCE) {
            return fare + ((Math.ceil((distance - FIRST_INCREASING_DISTANCE) / FIRST_UNIT)) * ADDITIONAL_AMOUNT);
        }
        if (distance > SECOND_INCREASING_DISTANCE) {
            return fare + FIRST_MAX_AMOUNT + ((Math.ceil((distance - SECOND_INCREASING_DISTANCE) / SECOND_UNIT)) * ADDITIONAL_AMOUNT);
        }
        return fare;
    }
}
