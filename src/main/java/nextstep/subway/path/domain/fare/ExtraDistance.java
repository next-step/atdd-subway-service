package nextstep.subway.path.domain.fare;

import java.util.Arrays;

public enum ExtraDistance {

    OVER_10KM(10, 50, 10, 5),
    OVER_50KM(50, Integer.MAX_VALUE, 10, 8);

    private final int minDistance;
    private final int maxDistance;
    private final int baseDistance;
    private final int threshold;

    ExtraDistance(int minDistance, int maxDistance, int baseDistance, int threshold) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.baseDistance = baseDistance;
        this.threshold = threshold;
    }

    public static int getOverDistance(int distance) {
        Double overDistance = Arrays.stream(values())
                .filter(extra -> (distance > extra.minDistance) && (distance <= extra.maxDistance))
                .findFirst()
                .map(value -> (double) (distance - value.baseDistance) / value.threshold)
                .orElse(0.0);

        return (int) Math.ceil(overDistance);
    }
}
