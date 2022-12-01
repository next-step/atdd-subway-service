package nextstep.subway.fare.domain;

import nextstep.subway.fare.NegativeDistanceException;
import nextstep.subway.message.ExceptionMessage;

import static nextstep.subway.fare.domain.FareConstant.*;
import static nextstep.subway.fare.domain.OverFarePolicy.OVER_FIFTY;
import static nextstep.subway.fare.domain.OverFarePolicy.TO_FIFTY;

public class SubwayFareCalculator implements FareCalculator {

    private static final int MIN_LINE_FARE = 0;
    private static final int MIN_ADULT_AGE = 19;

    private int lineFare;
    private int age;

    public SubwayFareCalculator() {
        this.lineFare = MIN_LINE_FARE;
        this.age = MIN_ADULT_AGE;
    }

    private SubwayFareCalculator(int lineFare, int age) {
        this.lineFare = lineFare;
        this.age = age;
    }

    public static SubwayFareCalculator from(int lineFare) {
        return new SubwayFareCalculator(lineFare, MIN_ADULT_AGE);
    }

    public static SubwayFareCalculator of(int lineFare, int age) {
        return new SubwayFareCalculator(lineFare, age);
    }

    @Override
    public int calculate(int distance) {
        checkDistanceNotNegative(distance);

        int fare = calculateWithDistance(distance);
        fare += lineFare;

        AgeFarePolicy policy = AgeFarePolicy.findByAge(age);
        return policy.discount(fare);
    }

    private int calculateWithDistance(int distance) {
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
