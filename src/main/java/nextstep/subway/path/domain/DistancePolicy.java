package nextstep.subway.path.domain;

import java.util.stream.Stream;

public enum DistancePolicy {
    TEN_TO_FIFTY(10, 50, 5),
    MAX_OVER(50, Integer.MAX_VALUE, 8);

    private static final int STANDARD_FARE = 100;

    private final int start;
    private final int end;
    private final int standardDistance;

    DistancePolicy(int start, int end, int standardDistance) {
        this.start = start;
        this.end = end;
        this.standardDistance = standardDistance;
    }

    public static int getOverFare(int distance) {
        return Stream.of(values())
            .filter(it -> it.isFartherThenStart(distance))
            .mapToInt(it -> it.calculateOverFare(distance))
            .sum();
    }

    private boolean isFartherThenStart(int distance) {
        return start < distance;
    }

    private int conversionTo(int distance) {
        if (isFartherThenEnd(distance)) {
            return end - start;
        }
        return distance - start;
    }

    private boolean isFartherThenEnd(int distance) {
        return end < distance;
    }

    private int calculateOverFare(int distance) {
        return (int)((Math.ceil((conversionTo(distance) - 1) / standardDistance) + 1) * STANDARD_FARE);
    }

}
