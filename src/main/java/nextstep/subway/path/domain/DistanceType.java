package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Distance;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DistanceType {
    BASIC(distance -> distance <= Base.FIRST_SURCHARGE_SECTION,
            distance -> 0),
    MIDDLE(distance -> distance > Base.FIRST_SURCHARGE_SECTION && distance <= Base.SECOND_SURCHARGE_SECTION,
            distance -> calculate(distance, Base.FIRST_SURCHARGE_SECTION, Base.FIRST_SURCHARGE_PER_DISTANCE)),
    LONG(distance -> distance > Base.SECOND_SURCHARGE_SECTION,
            distance -> calculate(distance, Base.SECOND_SURCHARGE_SECTION, Base.SECOND_SURCHARGE_PER_DISTANCE) + MIDDLE.calculateDistanceFare(Base.SECOND_SURCHARGE_SECTION));

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

    private static int calculate(int distance, int basedSurchargeSectionDistance, int perDistance) {
        return (int) ((Math.ceil((distance - basedSurchargeSectionDistance - 1) / perDistance) + 1) * 100);
    }

    static class Base {
        public static final int FIRST_SURCHARGE_SECTION = 10;
        public static final int SECOND_SURCHARGE_SECTION = 50;
        public static final int FIRST_SURCHARGE_PER_DISTANCE = 5;
        public static final int SECOND_SURCHARGE_PER_DISTANCE = 8;
    }

}
