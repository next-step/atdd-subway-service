package nextstep.subway.fare.domain.distance;

import java.util.Arrays;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.fare.domain.distance.impl.BasicDistancePolicy;
import nextstep.subway.fare.domain.distance.impl.LongDistancePolicy;
import nextstep.subway.fare.domain.distance.impl.MiddleDistancePolicy;

public enum DistanceFareType {
    BASIC(BasicDistancePolicy.getInstance()),
    BETWEEN_10KM_AND_50KM(MiddleDistancePolicy.getInstance()),
    OVER_50KM(LongDistancePolicy.getInstance());

    private final DistancePolicy distancePolicy;

    DistanceFareType(DistancePolicy distancePolicy) {
        this.distancePolicy = distancePolicy;
    }

    public static DistancePolicy getDistancePolicy(int distance) {
        return Arrays.stream(values())
            .filter(type -> type.distancePolicy.includeDistance(distance))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(ExceptionType.INVALID_DISTANCE))
            .getDistancePolicy();
    }

    public DistancePolicy getDistancePolicy() {
        return distancePolicy;
    }
}
