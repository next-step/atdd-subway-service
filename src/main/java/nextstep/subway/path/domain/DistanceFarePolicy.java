package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum DistanceFarePolicy {
    OVER_FIFTY_KM(50, distance -> (int) ((Math.ceil((distance - 1) / 8) + 1) * 100)),
    OVER_TEN_KM(10, distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)),
    DEFAULT(0, distance -> 1250);

    private final int conditionDistance;
    private final Function<Integer, Integer> calculation;

    DistanceFarePolicy(int conditionDistance, Function<Integer, Integer> calculation) {
        this.conditionDistance = conditionDistance;
        this.calculation = calculation;
    }

    public static Fare calculate(int distance) {
        int fareValue = 0;
        int distanceValue = distance;
        for (DistanceFarePolicy policy : findMatches(distanceValue)) {
            fareValue += calculate(policy, distanceValue);
            distanceValue = policy.conditionDistance;
        }
        return new Fare(fareValue);
    }

    private static List<DistanceFarePolicy> findMatches(int distance) {
        return Arrays.stream(values())
                     .filter(it -> distance > it.conditionDistance)
                     .collect(Collectors.toList());
    }

    private static int calculate(DistanceFarePolicy distanceFare, int distanceValue) {
        return distanceFare.calculation.apply(distanceValue - distanceFare.conditionDistance).intValue();
    }
}
