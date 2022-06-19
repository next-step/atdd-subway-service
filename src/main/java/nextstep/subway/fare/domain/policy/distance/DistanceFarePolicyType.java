package nextstep.subway.fare.domain.policy.distance;

import java.util.Arrays;
import nextstep.subway.fare.domain.policy.distance.impl.DefaultDistanceFarePolicy;
import nextstep.subway.fare.domain.policy.distance.impl.LongDistanceFarePolicy;
import nextstep.subway.fare.domain.policy.distance.impl.MiddleDistanceFarePolicy;

public enum DistanceFarePolicyType {

    DEFAULT(DefaultDistanceFarePolicy.getInstance()),
    Middle(MiddleDistanceFarePolicy.getInstance()),
    LONG(LongDistanceFarePolicy.getInstance());

    public static final int DEFAULT_DISTANCE_ADDITIONAL_FARE = 100;
    public static final int DEFAULT_MAX_DISTANCE = 10;
    public static final int MIDDLE_MAX_DISTANCE = 50;

    private final DistanceFarePolicy distanceFarePolicy;

    DistanceFarePolicyType(DistanceFarePolicy distanceFarePolicyClass) {
        this.distanceFarePolicy = distanceFarePolicyClass;
    }

    public static DistanceFarePolicy getDistanceFarePolicy(int distance) {
        return Arrays.stream(values())
            .filter(type -> type.distanceFarePolicy.includeDistance(distance))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException("거리에 대한 요금정책이 존재하지 않습니다."))
            .getDistanceFarePolicy();
    }

    public DistanceFarePolicy getDistanceFarePolicy() {
        return distanceFarePolicy;
    }

}
