package nextstep.subway.path.domain;

import java.util.function.Function;

public enum PathFare {
    DEFAULT(10, (distance) -> PathFareCalculator.calculateDefaultFare()),
    MIDDLE(50, (distance) -> PathFareCalculator.calculateMiddleFare(distance)),
    LONG(Integer.MAX_VALUE, (distance) -> PathFareCalculator.calculateLongFare(distance));

    private int maxDistance;
    private Function<Integer, Integer> expression;

    PathFare(int maxDistance, Function<Integer, Integer> expression) {
        this.maxDistance = maxDistance;
        this.expression = expression;
    }

    public static PathFare match(int distance) {
        if(isMiddleRange(distance)) {
            return PathFare.MIDDLE;
        }

        if(isLongRange(distance)) {
            return PathFare.LONG;
        }

        return PathFare.DEFAULT;
    }

    private static boolean isMiddleRange(int distance) {
        return distance > DEFAULT.maxDistance && distance <= MIDDLE.maxDistance;
    }

    private static boolean isLongRange(int distance) {
        return distance > MIDDLE.maxDistance;
    }

    public Integer getFare(Integer distance) {
        return this.expression.apply(distance);
    }

    public int getMaxDistance() {
        return maxDistance;
    }
}
