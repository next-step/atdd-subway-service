package nextstep.subway.line.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum AddedFarePolicyByDistance {
    OVER_FIFTY(50, distance -> calculateFare(distance, 8)),
    OVER_TEN(10, distance -> calculateFare(distance, 5)),
    OVER_ZERO(0, distance -> BigDecimal.valueOf(1250)),
    ;

    private final int distance;
    private final Function<Integer, BigDecimal> expression;

    AddedFarePolicyByDistance(int distance, Function<Integer, BigDecimal> expression) {
        this.distance = distance;
        this.expression = expression;
    }

    public static Fare calculate(int distance) {
        BigDecimal fare = BigDecimal.ZERO;
        for (AddedFarePolicyByDistance policy : findPolicies(distance)) {
            fare = fare.add(policy.expression.apply(distance - policy.distance));
            distance = policy.distance;
        }
        return Fare.from(fare.intValue());
    }

    private static List<AddedFarePolicyByDistance> findPolicies(int distance) {
        return Arrays.stream(AddedFarePolicyByDistance.values())
                .filter(addedFarePolicyByDistance -> addedFarePolicyByDistance.distance < distance)
                .collect(Collectors.toList());
    }

    private static BigDecimal calculateFare(Integer distance, int val) {
        return BigDecimal.valueOf(distance).subtract(BigDecimal.ONE)
                .divide(BigDecimal.valueOf(val), RoundingMode.DOWN)
                .add(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100));
    }
}
