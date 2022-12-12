package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AddedFarePolicyByDistance {
    거리_50km_초과(50, distance -> (int) ((Math.ceil((distance - 1) / 8) + 1) * 100)),
    거리_10km_초과_50km_이하(10, distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)),
    거리_0km_초과_10km_이하(0, distance -> 1250),
    ;

    private final int distance;
    private final Function<Integer, Integer> expression;

    AddedFarePolicyByDistance(int distance, Function<Integer, Integer> expression) {
        this.distance = distance;
        this.expression = expression;
    }

    public static Fare calculate(int distance) {
        int fare = 0;
        for (AddedFarePolicyByDistance policy : findPolicies(distance)) {
            fare += policy.expression.apply(distance - policy.distance);
            distance = policy.distance;
        }
        return Fare.from(fare);
    }

    private static List<AddedFarePolicyByDistance> findPolicies(int distance) {
        return Arrays.stream(AddedFarePolicyByDistance.values())
                .filter(addedFarePolicyByDistance -> addedFarePolicyByDistance.distance < distance)
                .collect(Collectors.toList());
    }
}
