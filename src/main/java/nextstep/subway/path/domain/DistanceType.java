package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceType {
    BASIC(distance -> distance <= 10, distance -> 0),
    MIDDLE(distance -> distance > 10 && distance <= 50, distance -> calculate(distance, 10, 5)),
    LONG(distance -> distance > 50, distance -> calculate(distance, 50, 8) + MIDDLE.calculateDistanceFare(50));

    private final Predicate<Integer> distanceMatchingExp;
    private final Function<Integer, Integer> distanceCalculateFunc;

    DistanceType(Predicate<Integer> distanceMatchingExp, Function<Integer, Integer> distanceCalculateExp) {
        this.distanceMatchingExp = distanceMatchingExp;
        this.distanceCalculateFunc = distanceCalculateExp;
    }

    public static DistanceType of(Distance distance) {
        return Arrays.stream(values())
                .filter(d -> d.distanceMatchingExp.test(distance.toInt()))
                .findFirst()
                .orElse(LONG);
    }

    public int calculateDistanceFare(int distance) {
        return distanceCalculateFunc.apply(distance);
    }

    private static int calculate(int distance, int basedDistance, int fareDistance) {
        return (int) ((Math.ceil((distance - basedDistance - 1) / fareDistance) + 1) * 100);
    }


}
