package nextstep.subway.path.domain;

public enum DistancePolicy {

    MEDIUM(10, 50, 5),
    LONG(50, Integer.MAX_VALUE, 8);

    public static final int BASE_FARE = 1250;
    private static final int ADD_FARE = 100;

    private final int minDistance;
    private final int maxDistance;
    private final int perKm;

    DistancePolicy(int minDistance, int maxDistance, int perKm) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.perKm = perKm;
    }

    public int calculateFare(int distance) {
        if (distance <= minDistance) {
            return 0;
        }
        return calculateOverFare(calculateOverDistance(distance), perKm);
    }

    private int calculateOverFare(int overDistance, int perKm) {
        return (int) ((Math.ceil((overDistance - 1) / perKm) + 1) * ADD_FARE);
    }

    private int calculateOverDistance(int distance) {
        int overDistance = distance - minDistance;
        if (overDistance > maxDistance - minDistance) {
            overDistance = maxDistance - minDistance;
        }
        return overDistance;
    }
}
