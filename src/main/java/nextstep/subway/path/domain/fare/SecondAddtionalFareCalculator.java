package nextstep.subway.path.domain.fare;

import nextstep.subway.path.domain.common.FareCalculateUtils;

public class SecondAddtionalFareCalculator implements FareCalculator {

    private static final int MIN_DISTANCE = 50;
    private static final int INTERVAL_OF_DISTANCE = 8;
    private static final int INTERVAL_FARE = 100;

    @Override
    public int calculate(int distance) {
        int targetDistance = distance - MIN_DISTANCE;
        if (targetDistance > 0) {
            return FareCalculateUtils.getTotalFareOfDistance(targetDistance, INTERVAL_OF_DISTANCE, INTERVAL_FARE);
        }
        return 0;
    }
}
