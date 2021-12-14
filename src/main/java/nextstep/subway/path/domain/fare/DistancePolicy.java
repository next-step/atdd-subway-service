package nextstep.subway.path.domain.fare;

public class DistancePolicy {
    private static final int BASE_DISTANCE = 10;
    private static final int OVER_DISTANCE_10KM = 10;
    private static final int OVER_DISTANCE_50KM = 50;
    private static final int THRESHOLD_EVERY_10KM = 5;
    private static final int THRESHOLD_EVERY_50KM = 8;

    public int getOverDistance(int distance) {
        double overDistance = 0;
        if (OVER_DISTANCE_10KM < distance) {
            overDistance = (double) (distance - BASE_DISTANCE) / THRESHOLD_EVERY_10KM;
        }
        if (OVER_DISTANCE_50KM < distance) {
            overDistance = (double) (distance - BASE_DISTANCE) / THRESHOLD_EVERY_50KM;
        }
        return (int) Math.ceil(overDistance);
    }
}
