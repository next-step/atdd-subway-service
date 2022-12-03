package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import nextstep.subway.line.domain.ExtraFare;
import nextstep.subway.path.domain.policy.BasicDistancePolicy;
import nextstep.subway.path.domain.policy.DistancePolicy;
import nextstep.subway.path.domain.policy.LongDistancePolicy;
import nextstep.subway.path.domain.policy.MiddleDistancePolicy;

public enum DistanceDiscount {
    BASIC(0, 10, distance -> new BasicDistancePolicy()),
    MIDDLE(11, 50, distance -> new MiddleDistancePolicy(new BasicDistancePolicy(), distance - BASIC.end)),
    LONG(51, 178, distance -> new LongDistancePolicy(new MiddleDistancePolicy(new BasicDistancePolicy(), MIDDLE.end - BASIC.end), distance - MIDDLE.end));

    private final int start;
    private final int end;
    private final Function<Integer, DistancePolicy> expression;

    DistanceDiscount(int start, int end, Function<Integer, DistancePolicy> expression) {
        this.start = start;
        this.end = end;
        this.expression = expression;
    }

    public static ExtraFare calculate(int distance) {
        DistanceDiscount distanceDiscount = findByDistance(distance);
        return ExtraFare.from(distanceDiscount.expression.apply(distance).calculate());
    }

    private static DistanceDiscount findByDistance(int distance) {
        return Arrays.stream(DistanceDiscount.values())
            .filter(fareDistance -> fareDistance.isBetween(distance))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isBetween(int distance) {
        return start <= distance && distance <= end;
    }
}
