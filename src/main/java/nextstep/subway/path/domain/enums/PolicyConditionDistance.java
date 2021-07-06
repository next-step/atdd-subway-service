package nextstep.subway.path.domain.enums;


import java.util.Arrays;
import java.util.function.Predicate;

public enum PolicyConditionDistance {
    FIRST_CONDITION(distance -> distance > 10 && distance <= 50, 10, 5, 0),
    SECOND_CONDITION(distance -> distance > 50, 50, 8, 800);

    private final Predicate<Double> predicate;
    private final int increasingDistance;
    private final int increasingUnit;
    private final int prevMaxFare;

    PolicyConditionDistance(Predicate<Double> predicate, int increasingDistance, int increasingUnit, int prevMaxFare) {
        this.predicate = predicate;
        this.increasingDistance = increasingDistance;
        this.increasingUnit = increasingUnit;
        this.prevMaxFare = prevMaxFare;
    }

    public static PolicyConditionDistance findCondition(double distance) {
        return Arrays.stream(values())
                .filter(condition -> condition.predicate.test(distance))
                .findFirst()
                .get();
    }

    public int getIncreasingDistance() {
        return increasingDistance;
    }

    public int getIncreasingUnit() {
        return increasingUnit;
    }

    public int getPrevMaxFare() {
        return prevMaxFare;
    }
}
