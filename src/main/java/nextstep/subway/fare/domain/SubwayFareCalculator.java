package nextstep.subway.fare.domain;

import nextstep.subway.fare.NegativeDistanceException;
import nextstep.subway.message.ExceptionMessage;

import static nextstep.subway.fare.domain.FareConstant.*;
import static nextstep.subway.fare.domain.OverFarePolicy.OVER_FIFTY;
import static nextstep.subway.fare.domain.OverFarePolicy.TO_FIFTY;

public class SubwayFareCalculator implements FareCalculator {

    private static final int MIN_LINE_FARE = 0;
    private int lineFare;

    public SubwayFareCalculator() {
        this.lineFare = MIN_LINE_FARE;
    }

    private SubwayFareCalculator(int lineFare) {
        this.lineFare = lineFare;
    }

    public static SubwayFareCalculator from(int lineFare) {
        return new SubwayFareCalculator(lineFare);
    }

    @Override
    public int calculate(int distance) {
        checkDistanceNotNegative(distance);

        int fare = calculateWithDistance(distance);
        fare += lineFare;

        return fare;
    }

    private static int calculateWithDistance(int distance) {
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
