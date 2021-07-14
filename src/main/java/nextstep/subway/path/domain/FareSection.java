package nextstep.subway.path.domain;

import static nextstep.subway.common.constants.FareConstants.*;

public enum FareSection {
    BASIC(10, 1, 0L),
    ADVANCED(40, 5, 100L),
    FINAL(Integer.MAX_VALUE, 8, 100L);

    private int maxDistance;
    private int unitDistance;
    private long unitFare;

    FareSection(int maxDistance, int unitDistance, long unitFare) {
        this.maxDistance = maxDistance;
        this.unitDistance = unitDistance;
        this.unitFare = unitFare;
    }

    public int maxDistance() {
        return maxDistance;
    }

    private long calculate(int distance) {
        if (distance > 0) {
            int calculateDistance = Math.min(distance, maxDistance);
            return calculateDistance / unitDistance * unitFare;
        }
        return MIN_FARE;
    }
    public static Fare calculateFare(int distance) {
        long calculateFare = BASIC_FARE;

        for (FareSection section : FareSection.values()) {
            calculateFare += section.calculate(distance);
            distance -= section.maxDistance();
        }
        return new Fare(calculateFare);
    }
}
