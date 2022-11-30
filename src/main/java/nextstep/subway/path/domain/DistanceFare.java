package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import nextstep.subway.line.domain.Distance;

public enum DistanceFare {
    OVER_50(50, 8, DistanceFare::calculateOver50),
    OVER_10(10, 5, DistanceFare::calculateOver10),
    DEFAULT(1, 0, distance -> 0);

    private static final int OVER_FARE = 100;

    private final Distance minDistance;
    private final int overDistance;
    private final Function<Distance, Integer> expression;

    DistanceFare(int minDistance, int overDistance, Function<Distance, Integer> expression) {
        this.minDistance = new Distance(minDistance);
        this.overDistance = overDistance;
        this.expression = expression;
    }

    public static Fare calculate(Distance distance) {
        DistanceFare distanceFare = find(distance);
        return new Fare(distanceFare.expression.apply(distance));
    }

    private static DistanceFare find(Distance distance) {
        return Arrays.stream(values())
                .filter(policy -> distance.isOver(policy.minDistance))
                .findFirst()
                .orElse(DEFAULT);
    }

    private static int calculateOver10(Distance distance) {
        distance = distance.subtract(OVER_10.minDistance);
        return (int) ((Math.ceil((distance.value() - 1) / OVER_10.overDistance) + 1) * OVER_FARE);
    }

    private static int calculateOver50(Distance distance) {
        distance = distance.subtract(OVER_50.minDistance);
        return (int) ((Math.ceil((distance.value() - 1) / OVER_50.overDistance) + 1) * OVER_FARE) + calculateOver10(OVER_50.minDistance);
    }
}
