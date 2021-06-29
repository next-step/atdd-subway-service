package nextstep.subway.path.domain;

import java.util.Arrays;

public enum Surcharge {
    DISTANCE_OVER_50KM(50, 8, 100),
    DISTANCE_OVER_10KM(10, 5, 100),
    NONE(0, 0, 0);

    private int distanceBoundary;
    private int addDistance;
    private int addFare;

    Surcharge(int distanceBoundary, int addDistance, int addFare) {
        this.distanceBoundary = distanceBoundary;
        this.addDistance = addDistance;
        this.addFare = addFare;
    }

    public static Surcharge findSurchargeByDistance(int distance) {
        return Arrays.stream(values())
            .filter(surcharge -> surcharge.ditanceOverBoundary(distance))
            .findFirst()
            .orElse(NONE);
    }

    public boolean ditanceOverBoundary(int distance) {
        return distanceBoundary < distance;
    }

    public int getDistanceBoundary() {
        return distanceBoundary;
    }

    public int getAddDistance() {
        return addDistance;
    }

    public int getAddFare() {
        return addFare;
    }

}
