package nextstep.subway.fare.domain;

import static nextstep.subway.fare.domain.FareConstant.BASIC_FARE;
import static nextstep.subway.fare.domain.FareConstant.MIN_DISTANCE_OF_FIRST_OVER_FARE_SECTION;

public class SubwayFareCalculator implements FareCalculator {
    @Override
    public int calculate(int distance) {
        if (distance <= MIN_DISTANCE_OF_FIRST_OVER_FARE_SECTION) {
            return BASIC_FARE;
        }
        return 0;
    }
}
