package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.common.FareCalculateUtils;

public class FirstAdditionalFareOfDistanceCalculator implements FareOfDistanceCalculator {

    private static final int MIN_DISTANCE = 10;
    private static final int MAX_DISTANCE = 50;
    private static final int INTERVAL_OF_DISTANCE = 5;
    private static final int INTERVAL_FARE = 100;

    @Override
    public int calculate(int distance) {
        int targetDistance = distance;
        if (targetDistance > MAX_DISTANCE) {
            targetDistance = MAX_DISTANCE;
        }
        targetDistance = targetDistance - MIN_DISTANCE;
        if (targetDistance > 0) {
            return FareCalculateUtils.getTotalFareOfDistance(targetDistance, INTERVAL_OF_DISTANCE, INTERVAL_FARE);
        }
        return 0;
    }
}
