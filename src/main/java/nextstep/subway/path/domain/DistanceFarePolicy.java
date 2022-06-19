package nextstep.subway.path.domain;

import java.util.Arrays;

public enum DistanceFarePolicy {
    LOW(1, 10, 0, 0, DistanceFarePolicyConstants.DEFAULT_FARE),
    MIDDLE(11, 50, 5, 100, DistanceFarePolicyConstants.DEFAULT_FARE),
    HIGH(51, 178, 8, 100, DistanceFarePolicyConstants.DEFAULT_HIGH_FARE);

    private static class DistanceFarePolicyConstants {
        private static final int DEFAULT_FARE = 1250;
        private static final int DEFAULT_HIGH_FARE = DEFAULT_FARE + (int) (
                (Math.ceil((MIDDLE.endDistance - MIDDLE.startDistance) / MIDDLE.calculateUnit) + 1)
                        * MIDDLE.overFarePerUnit);
    }

    private final int startDistance;
    private final int endDistance;
    private final int calculateUnit;
    private final int overFarePerUnit;
    private final int defaultFare;

    DistanceFarePolicy(int startDistance, int endDistance, int calculateUnit, int overFarePerUnit, int defaultFare) {
        this.startDistance = startDistance;
        this.endDistance = endDistance;
        this.calculateUnit = calculateUnit;
        this.overFarePerUnit = overFarePerUnit;
        this.defaultFare = defaultFare;
    }

    public static DistanceFarePolicy findByDistance(int distance) {
        return Arrays.stream(DistanceFarePolicy.values())
                .filter(distanceFareType -> distanceFareType.isIncludedRange(distance))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("비정상적인 이동 거리입니다."));
    }

    private boolean isIncludedRange(int distance) {
        return distance >= startDistance && distance <= endDistance;
    }

    public int calculate(int distance) {
        int fare = defaultFare;
        if(overFarePerUnit > 0) {
            int calculateDistance = distance - startDistance + 1;
            fare += (int) ((Math.ceil((calculateDistance - 1) / calculateUnit) + 1) * overFarePerUnit);
        }
        return fare;
    }
}
