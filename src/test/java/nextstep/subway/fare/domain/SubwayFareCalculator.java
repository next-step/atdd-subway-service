package nextstep.subway.fare.domain;

import nextstep.subway.fare.NegativeDistanceException;
import nextstep.subway.message.ExceptionMessage;

import static nextstep.subway.fare.domain.FareConstant.*;
import static nextstep.subway.fare.domain.OverFarePolicy.OVER_FIFTY;
import static nextstep.subway.fare.domain.OverFarePolicy.TO_FIFTY;

public class SubwayFareCalculator implements FareCalculator {

    @Override
    public int calculate(int distance) {
        checkDistanceNotNegative(distance);

        int fare = BASIC_FARE;

        OverFarePolicy policy = OverFarePolicy.findPolicyByDistance(distance);

        fare += policy.calculateOverFare(distance);

        if (policy == OVER_FIFTY) {
            fare += TO_FIFTY.calculateOverFare(MIN_DISTANCE_OF_SECOND_OVER_FARE_SECTION);
        }

        return fare;
    }

    private void checkDistanceNotNegative(int distance) {
        if (distance < MIN_DISTANCE_OF_BASIC_FARE_SECTION) {
            throw new NegativeDistanceException(ExceptionMessage.INVALID_DISTANCE);
        }
    }
}
