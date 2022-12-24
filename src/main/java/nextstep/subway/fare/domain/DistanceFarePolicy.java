package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DistanceFarePolicy {
    OVER_50KM(50, distance -> (int) ((Math.ceil((distance - 1) / 8) + 1) * 100)),
    OVER_10KM_UNDER50KM(10, distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)),
    UNDER10KM(0, distance -> 1250);

    private final int distance;
    private final Function<Integer, Integer> expression;

    DistanceFarePolicy(int distance, Function<Integer, Integer> expression) {
        this.distance = distance;
        this.expression = expression;
    }

    public static Fare calculate(int distance) {
        int fare = 0;
        for (DistanceFarePolicy policy : findPolicies(distance)) {
            fare += policy.expression.apply(distance - policy.distance);
            distance = policy.distance;
        }
        return Fare.from(fare);
    }

    private static List<DistanceFarePolicy> findPolicies(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFarePolicy -> distanceFarePolicy.distance < distance)
                .collect(Collectors.toList());
    }
}
