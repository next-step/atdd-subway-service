package nextstep.subway.policy;

import nextstep.subway.exception.InvalidDistanceRangeException;

import java.util.Arrays;
import java.util.Objects;

public enum DistanceType {
    DEFAULT(0, 10, 0, 0, null),
    TEN_OVER(10, 50, 5, 100, DEFAULT),
    FIFTY_OVER(50, Integer.MAX_VALUE, 8, 100, TEN_OVER);

    private final int start;
    private final int end;
    private final int range;
    private final int fare;
    private final DistanceType beforeType;

    DistanceType(int start, int end, int range, int fare, DistanceType beforeType) {
        this.start = start;
        this.end = end;
        this.range = range;
        this.fare = fare;
        this.beforeType = beforeType;
    }

    public static DistanceType calculatorDistanceType(final int distance) {
        return Arrays.stream(values())
                .filter(distanceType -> distanceType.isDistance(distance))
                .findFirst()
                .orElseThrow(InvalidDistanceRangeException::new);
    }

    private boolean isDistance(final int distance) {
        return this.start < distance && this.end >= distance;
    }

    public int calculatorAdditionalFare(final int distance) {
        int totalFare = 0;
        totalFare = calculate(distance);

        DistanceType type = this.beforeType;
        while (!Objects.isNull(type)) {
            totalFare += type.calculate(type.end);
            type = type.beforeType;
        }

        return totalFare;
    }

    private int calculate(final int distance) {
        int overDistance = distance - start;
        return (int) ((Math.floor((double) (overDistance - 1) / range) + 1) * fare);
    }
}
