package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

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

    public static Fare calculate(int distance, Fare extraFare) {
        DistanceFarePolicy farePolicy = findPolicy(distance);
        Fare fare = Fare.of(BASIC_FARE).plus(extraFare);
        if (farePolicy.equals(DistanceFarePolicy.BASIC)) {
            return fare;
        }
        return fare.plus(Fare.of(farePolicy.calculateOverFare(distance)));
    }

    private int calculateOverFare(int distance) {
        return (int) ((Math.ceil((distance - min) / this.distance) + 1) * PER_FARE);
    }

    private static DistanceFarePolicy findPolicy(int distance) {
        return Arrays.stream(values())
                .filter(distanceFarePolicy -> distanceFarePolicy.min < distance && distanceFarePolicy.max >= distance)
                .findFirst()
                .orElse(BASIC);
    }
}
