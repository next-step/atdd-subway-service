package nextstep.subway.fare.domain.distance;

import java.util.Arrays;
import java.util.function.Predicate;
import nextstep.subway.exception.BadRequestException;
import nextstep.subway.exception.ExceptionType;
import nextstep.subway.fare.domain.distance.impl.BasicDistancePolicy;
import nextstep.subway.fare.domain.distance.impl.LongDistancePolicy;
import nextstep.subway.fare.domain.distance.impl.MiddleDistancePolicy;

public enum DistanceFareType {
    BASIC(distance -> distance <= 10, BasicDistancePolicy.getInstance()),
    BETWEEN_10KM_AND_50KM(distance -> distance > 10 && distance <= 50, MiddleDistancePolicy.getInstance()),
    OVER_50KM(distance -> distance >= 50, LongDistancePolicy.getInstance());

    private final Predicate<Integer> predicate;
    private final DistancePolicy distancePolicy;

    DistanceFareType(Predicate<Integer> predicate, DistancePolicy distancePolicy) {
        this.predicate = predicate;
        this.distancePolicy = distancePolicy;
    }

    public static DistancePolicy getDistancePolicy(int distance) {
        return Arrays.stream(values())
            .filter(type -> type.predicate.test(distance))
            .findFirst()
            .orElseThrow(() -> new BadRequestException(ExceptionType.INVALID_DISTANCE))
            .getDistancePolicy();
    }

    public DistancePolicy getDistancePolicy() {
        return distancePolicy;
    }
}
