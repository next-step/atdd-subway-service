package nextstep.subway.line.domain;

public enum DistanceFareUnit {
    DISTANCE_50KM(50, 8),
    DISTANCE_10KM(10, 5),
    DISTANCE_0KM(0, 0);

    private final int distanceBoundary;
    private final int distanceUnit;

    DistanceFareUnit(int distanceBoundary, int distanceUnit) {
        this.distanceBoundary = distanceBoundary;
        this.distanceUnit = distanceUnit;
    }

    public int ofBoundary() {
        return distanceBoundary;
    }

    public int fare(int distance) {
        if(isDefaultFareSection() || isZeroDistance(distance)) {
            return 0;
        }
        return (int) ((Math.ceil((distance - 1) / distanceUnit) + 1) * 100);
    }

    private boolean isZeroDistance(int distance) {
        return distance == 0;
    }

    private boolean isDefaultFareSection() {
        return distanceBoundary == 0;
    }

}
