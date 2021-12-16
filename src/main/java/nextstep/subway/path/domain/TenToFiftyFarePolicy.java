package nextstep.subway.path.domain;

public class TenToFiftyFarePolicy implements FarePolicy {
    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;
    private static final int ADDITIONAL_FARE = 100;
    private static final int DIVIDE_DISTANCE = 5;
    private static final int DIVIDE_HELPER_ONE = 1;
    private static final int BASE_ONE = 1;

    @Override
    public Fare calculateFare(int distance) {
        if (distance <= MIN_DISTANCE) {
            return Fare.ZERO;
        }

        distance = Math.min(distance, MAX_DISTANCE) - MIN_DISTANCE;

        return calculateOverFare(distance);
    }

    private Fare calculateOverFare(int distance) {
        int fare = ((distance - DIVIDE_HELPER_ONE) / DIVIDE_DISTANCE + BASE_ONE) * ADDITIONAL_FARE;
        return Fare.from(fare);
    }

}
