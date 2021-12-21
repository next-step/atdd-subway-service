package nextstep.subway.path.domain.fare;

public enum DistanceFarePolicy implements FarePolicy<Integer> {
    TEN_TO_FIFTY(10, 50, 100, 5),
    OVER_FIFTY(50, Integer.MAX_VALUE, 100, 8);

    private final int minDistance;
    private final int maxDistance;
    private final int additionalFare;
    private final int divideDistance;

    private static final int DIVIDE_HELPER_ONE = 1;
    private static final int BASE_ONE = 1;

    DistanceFarePolicy(int minDistance, int maxDistance, int additionalFare, int divideDistance) {
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.additionalFare = additionalFare;
        this.divideDistance = divideDistance;
    }

    @Override
    public Fare calculateFare(Integer distance) {
        if (distance <= minDistance) {
            return Fare.ZERO;
        }

        distance = Math.min(distance, maxDistance) - minDistance;

        return calculateOverFare(distance);
    }

    private Fare calculateOverFare(int distance) {
        int fare = ((distance - DIVIDE_HELPER_ONE) / divideDistance + BASE_ONE) * additionalFare;
        return Fare.from(fare);
    }
}
