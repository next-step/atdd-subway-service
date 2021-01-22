package nextstep.subway.path.domain.fare.distance;

public abstract class DistanceFare {
    public static final int NO_FARE = 0;
    private static final int ZERO_KM_DISTANCE = 0;

    public abstract int calculateFare();

    public int calculateOverFare(int distance, int kmByNumber) {
        if (distance < ZERO_KM_DISTANCE) {
            return NO_FARE;
        }
        return (int) ((Math.ceil((distance - 1) / kmByNumber) + 1) * 100);
    }
}
