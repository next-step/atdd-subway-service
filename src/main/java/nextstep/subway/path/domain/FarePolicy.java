package nextstep.subway.path.domain;

import java.util.Arrays;
import nextstep.subway.exception.InvalidArgumentException;

public enum FarePolicy {
    BASIC(0,9,0,0),
    ADDITIONAL_10(10, 49, 5, 0),
    ADDITIONAL_50(50, Integer.MAX_VALUE, 8, 800);

    private static final int BASE_FARE = 1250;
    private static final int PER_FARE = 100;
    private double min;
    private double max;
    private double distance;
    private int addFare;


    FarePolicy(int min, int max, int distance, int addFare) {
        this.min = min;
        this.max = max;
        this.distance = distance;
        this.addFare = addFare;
    }

    public static int calculateOverFare(int distance) {
        FarePolicy farePolicy = includePolicy(distance);

        if (farePolicy.equals(FarePolicy.BASIC)) {
            return BASE_FARE;
        }

        return BASE_FARE + farePolicy.calculate(distance)+farePolicy.addFare;
    }

    private int calculate(int distance) {
        double v = Math.ceil((distance - this.min) / this.distance) * PER_FARE;
        return Double.valueOf(v).intValue();
    }

    private static FarePolicy includePolicy(int distance) {
        return Arrays.stream(values())
            .filter(it -> it.min <= distance && it.max >= distance)
            .findFirst()
            .orElseThrow(() -> new InvalidArgumentException("포함되는 정책이 없습니다. 다시 입력해 주세요."));
    }
}
