package nextstep.subway.path.domain;

import nextstep.subway.line.exception.FareException;

import java.util.Arrays;

public enum DistanceFarePolicy {
    BASIC(0, 10, 0),
    ADDITIONAL_10(10, 50, 5),
    ADDITIONAL_50(50, Integer.MAX_VALUE, 8);

    private static final String POLICY_NOT_FOUND = "포함되는 정책이 없습니다.";
    private static final int BASIC_FARE = 1250;
    private static final int PER_FARE = 100;
    private int min;
    private int max;
    private int distance;

    DistanceFarePolicy(int min, int max, int distance) {
        this.min = min;
        this.max = max;
        this.distance = distance;
    }

    public static int calculate(int distance, int extraFare) {
        DistanceFarePolicy farePolicy = findPolicy(distance);

        if (farePolicy.equals(DistanceFarePolicy.BASIC)) {
            return BASIC_FARE + extraFare;
        }
        return BASIC_FARE + farePolicy.calculateOverFare(distance) + extraFare;
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - min) / this.distance) + 1) * PER_FARE);
    }

    private static DistanceFarePolicy findPolicy(int distance) {
        return Arrays.stream(values())
                .filter(it -> it.min < distance && it.max >= distance)
                .findFirst()
                .orElseThrow(() -> new FareException(POLICY_NOT_FOUND));
    }
}
