package nextstep.subway.path.domain;

public enum DistanceFeeType {
    NONE(0, 1.0),
    OVER10(10, 5.0),
    OVER50(50, 8.0);

    public static final int BASE_FEE = 1250;
    public static final int EXTRA_FEE = 100;
    public static final int MINIMUM_DISTANCE = 10;
    private final int maxDistance;
    private final double distance;

    DistanceFeeType(int maxDistance, double distance) {
        this.maxDistance = maxDistance;
        this.distance = distance;
    }

    public static DistanceFeeType getDistanceFeeType(int distance) {
        if (OVER50.maxDistance < distance) {
            return DistanceFeeType.OVER50;
        }

        if (OVER10.maxDistance < distance) {
            return DistanceFeeType.OVER10;
        }

        return NONE;
    }

    public static int calculateOverFare(int distance, DistanceFeeType distanceFeeType) {
        if (distance <= MINIMUM_DISTANCE) {
            return BASE_FEE;
        }

        return (int) (BASE_FEE +
                ((Math.floor(((distance - MINIMUM_DISTANCE) - 1) / distanceFeeType.distance) + 1)
                        * EXTRA_FEE));
    }

    public int getMaxDistance() {
        return maxDistance;
    }

    public double getDistance() {
        return distance;
    }
}
