package nextstep.subway.line.domain;

public class AdditionalFareCalculator {
    private final static int CRITERION_OF_DISTANCE = 50;
    private int overDistance;

    public AdditionalFareCalculator(int distance) {
        this.overDistance = distance;
    }

    public int getAdditionalFare() {
        int result = overDistance - CRITERION_OF_DISTANCE;
        if (hasOverFifty(result)) {
            int resultFare = calculateFiftyGreaterThan(result);
            resultFare += calculateTenGreaterThanFiftyLessThanEqual(CRITERION_OF_DISTANCE);
            return resultFare;
        }
        return calculateTenGreaterThanFiftyLessThanEqual(overDistance);
    }

    private int calculateTenGreaterThanFiftyLessThanEqual(int distance) {
        int partition = distance / 5;
        return 100 * partition;
    }

    private int calculateFiftyGreaterThan(int distance) {
        int partition = distance / 8;
        return 100 * partition;
    }

    private boolean hasOverFifty(int distance) {
        return distance > 0;
    }
}
