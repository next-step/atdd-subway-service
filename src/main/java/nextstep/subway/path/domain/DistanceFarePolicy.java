package nextstep.subway.path.domain;

import java.util.Arrays;
import nextstep.subway.line.domain.Distance;

public enum DistanceFarePolicy {
    DISTANCE_LIMIT_ONE(Distance.from(10), Distance.from(50), Distance.from(5), 100),
    DISTANCE_LIMIT_TWO(Distance.from(50), Distance.from(Integer.MAX_VALUE), Distance.from(8), 100);

    private final Distance minLimit;
    private final Distance maxLimit;
    private final Distance baseDistance;
    private final int overFarePerBase;

    DistanceFarePolicy(Distance minLimit, Distance maxLimit, Distance baseDistance, int overFarePerBase) {
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.baseDistance = baseDistance;
        this.overFarePerBase = overFarePerBase;
    }

    public static int calculateTotalExcessFare(Distance distance) {
        return Arrays.stream(values())
                .map(distanceFarePolicy -> distanceFarePolicy.calculateDistanceFare(distance))
                .reduce(0, Integer::sum);
    }

    private int calculateDistanceFare(Distance distance) {
        Distance excessDistance = calculateExcessDistance(distance);
        return calculateExcessFare(excessDistance);
    }

    private Distance calculateExcessDistance(Distance distance) {
        Distance excessDistance = distance.isGreaterThan(minLimit) ? distance.minus(minLimit) : Distance.ZERO;
        Distance maxExcessDistance = maxLimit.minus(minLimit);
        return Distance.min(excessDistance, maxExcessDistance);
    }

    private int calculateExcessFare(Distance excessDistance) {
        if (excessDistance == Distance.ZERO) {
            return 0;
        }
        return (int) ((Math.ceil((excessDistance.value() - 1) / baseDistance.value()) + 1) * overFarePerBase);
    }
}
