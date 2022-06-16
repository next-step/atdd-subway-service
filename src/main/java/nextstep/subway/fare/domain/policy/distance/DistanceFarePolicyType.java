package nextstep.subway.fare.domain.policy.distance;

import java.util.Arrays;
import nextstep.subway.fare.domain.policy.distance.impl.DefaultDistanceFarePolicy;
import nextstep.subway.fare.domain.policy.distance.impl.LongDistanceFarePolicy;
import nextstep.subway.fare.domain.policy.distance.impl.MiddleDistanceFarePolicy;

public enum DistanceFarePolicyType {

    DEFAULT(DefaultDistanceFarePolicy.class),
    BETWEEN_10KM_50KM(MiddleDistanceFarePolicy.class),
    OVER_50KM(LongDistanceFarePolicy.class);

    private final Class<? extends DistanceFarePolicy> distanceFarePolicy;
    DistanceFarePolicyType(Class<? extends DistanceFarePolicy> distanceFarePolicyClass) {
        this.distanceFarePolicy = distanceFarePolicyClass;
    }

    public static DistanceFarePolicy getDistanceFarePolicy(int distance) {
        return Arrays.stream(values())
            .map(DistanceFarePolicyType::getDistanceFarePolicyInstance)
            .filter(policy -> policy.isDistance(distance))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("거리에 대한 요금정책이 존재하지 않습니다."));
    }

    private static DistanceFarePolicy getDistanceFarePolicyInstance(DistanceFarePolicyType type) {
        try {
            return type.distanceFarePolicy.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

}
