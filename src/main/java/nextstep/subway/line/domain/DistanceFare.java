package nextstep.subway.line.domain;

import java.util.function.Function;

public enum DistanceFare {
    OVER_FIFTY_KM(50, distance -> (int) ((Math.ceil((distance - 1) / 8) + 1) * 100)),
    OVER_TEN_KM(10, distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)),
    DEFAULT(0, distance -> 1250);

    private final int conditionDistance;
    private final Function<Integer, Integer> calculation;

    DistanceFare(int conditionDistance, Function<Integer, Integer> calculation) {
        this.conditionDistance = conditionDistance;
        this.calculation = calculation;
    }

    public static Fare calculate(Distance distance) {
        int fareValue = 0;
        int distanceValue = distance.get();
        for (DistanceFare distanceFare : values()) {
            if (distanceValue > distanceFare.conditionDistance) {
                fareValue += calculate(distanceFare, distanceValue);
                distanceValue = distanceFare.conditionDistance;
            }
        }
        return new Fare(fareValue);
    }

    private static int calculate(DistanceFare distanceFare, int distanceValue) {
        return distanceFare.calculation.apply(distanceValue - distanceFare.conditionDistance).intValue();
    }
}
