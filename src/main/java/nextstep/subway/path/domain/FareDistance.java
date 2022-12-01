package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;
import nextstep.subway.path.domain.decorator.BasicDistancePolicy;
import nextstep.subway.path.domain.decorator.DistancePolicy;
import nextstep.subway.path.domain.decorator.LongDistancePolicy;
import nextstep.subway.path.domain.decorator.MiddleDistancePolicy;

import java.util.Arrays;
import java.util.function.Function;

public enum FareDistance {
    BASIC(0, 10, distance -> new BasicDistancePolicy()),
    MIDDLE(11, 50, distance ->
            new MiddleDistancePolicy(new BasicDistancePolicy(), distance - BASIC.end)),
    LONG(51, 178, distance ->
            new LongDistancePolicy(
                    new MiddleDistancePolicy(new BasicDistancePolicy(), MIDDLE.end - BASIC.end),
                    distance - MIDDLE.end
            )
    );

    private final int start;
    private final int end;
    private final Function<Integer, DistancePolicy> expression;

    FareDistance(int start, int end, Function<Integer, DistancePolicy> expression) {
        this.start = start;
        this.end = end;
        this.expression = expression;
    }

    public static Fare calculate(int distance) {
        FareDistance fareDistance = findByDistance(distance);
        return new Fare(fareDistance.expression.apply(distance).calculate());
    }

    private static FareDistance findByDistance(int distance) {
        return Arrays.stream(FareDistance.values())
                .filter(fareDistance -> fareDistance.isBetween(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    private boolean isBetween(int distance) {
        return start <= distance && distance <= end;
    }
}
