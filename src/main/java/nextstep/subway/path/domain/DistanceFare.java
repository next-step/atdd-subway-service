package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceFare {
    STANDARD(1, 10, distance -> 1250),
    FIRST_EXTRA(11, 50, distance -> {
        int standardFare = STANDARD.calculateFare(STANDARD.end);
        int extraFare = calculateOverFare(distance - STANDARD.end, 5);
        return standardFare + extraFare;
    }),
    SECOND_EXTRA(51, Integer.MAX_VALUE, distance -> {
        int firstExtraFare = FIRST_EXTRA.calculateFare(FIRST_EXTRA.end);
        int extraFare = calculateOverFare(distance - FIRST_EXTRA.end, 8);
        return firstExtraFare + extraFare;
    });

    public static final String DISTANCE_RANGE_OVER_EXCEPTION_MESSAGE = "최단 경로 거리는 0이하 일 수 없습니다.";

    private final int start;
    private final int end;
    private final DistanceFareStrategy distanceFareStrategy;

    DistanceFare(int start, int end, DistanceFareStrategy distanceFareStrategy) {
        this.start = start;
        this.end = end;
        this.distanceFareStrategy = distanceFareStrategy;
    }

    private static int calculateOverFare(int distance, int eachDistance) {
        return (int) ((Math.ceil((distance - 1) / eachDistance) + 1) * 100);
    }

    public int calculateFare(int distance) {
        return distanceFareStrategy.calculateDistanceFare(distance);
    }

    public static DistanceFare findDistanceFareByDistance(int distance) {
        return Arrays.stream(DistanceFare.values())
                .filter(distanceFare -> distanceFare.isWithinRange(distance))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(DISTANCE_RANGE_OVER_EXCEPTION_MESSAGE));
    }

    private boolean isWithinRange(int distance) {
        return start <= distance && distance <= end;
    }
}
