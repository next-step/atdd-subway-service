package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public enum DistanceFarePolicy {
    DISTANCE_OVER_50(50, distance -> distance > 50, distance -> (int) ((Math.ceil((distance - 1) / 8) + 1) * 100)),
    DISTANCE_BETWEEN_10_AND_50(10, distance -> distance > 10,
            distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)),
    DISTANCE_BASE(0, distance -> distance > 0, distance -> 1250);

    private final int minDistance;
    private Predicate<Integer> distanceCondition;
    private Function<Integer, Integer> calculationWithDistance;

    DistanceFarePolicy(int minDistance, Predicate<Integer> distanceCondition,
                       Function<Integer, Integer> calculationWithDistance) {
        this.minDistance = minDistance;
        this.distanceCondition = distanceCondition;
        this.calculationWithDistance = calculationWithDistance;
    }

    public static int calculateFare(final int distance) {
        int totalFare = 0;

        List<DistanceFarePolicy> policies = Arrays.stream(values()).
                filter(policy -> policy.distanceCondition.test(distance)).collect(Collectors.toList());

        int edgeDistance = distance;
        for (DistanceFarePolicy policy : policies) {
            totalFare += policy.calculationWithDistance.apply(edgeDistance - policy.minDistance);
            edgeDistance = policy.minDistance;
        }
        return totalFare;
    }

}
