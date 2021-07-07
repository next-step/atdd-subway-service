package nextstep.subway.path.domain.impl;

import nextstep.subway.path.domain.FarePolicy;
import nextstep.subway.path.domain.enums.PolicyConditionDistance;

/**
 * - 기본운임(10㎞ 이내) : 기본운임 1,250원
 * - 이용 거리초과 시 추가운임 부과
 * - 10km초과∼50km까지(5km마다 100원)
 * - 50km초과 시 (8km마다 100원)
 */
public class FarePolicyByDistance implements FarePolicy {
    private final double distance;
    private static final int ADDITIONAL_AMOUNT = 100;
    private static final int DEFAULT_DISTANCE = 10;

    public FarePolicyByDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public double calculate(double fare) {
        if(distance <= DEFAULT_DISTANCE) {
            return fare;
        }
        PolicyConditionDistance condition = PolicyConditionDistance.findCondition(distance);
        return fare + condition.getPrevMaxFare()
                + ((Math.ceil((distance - condition.getIncreasingDistance()) / condition.getIncreasingUnit())) * ADDITIONAL_AMOUNT);
    }
}
