package nextstep.subway.fare.domain;


import nextstep.subway.exception.FareValidException;

import static nextstep.subway.utils.Constant.MIN_DISTANCE_OF_BASIC_FARE_SECTION;
import static nextstep.subway.utils.Message.INVALID_OVER_DISTANCE;

public class FareCalculator {

    private static final int MIN_LINE_FARE = 0;
    private static final int MIN_ADULT_AGE = 19;

    private int lineFare;
    private int age;

    public FareCalculator() {
        this.lineFare = MIN_LINE_FARE;
        this.age = MIN_ADULT_AGE;
    }

    private FareCalculator(int lineFare, int age) {
        this.lineFare = lineFare;
        this.age = age;
    }

    public static FareCalculator from(int lineFare) {
        return new FareCalculator(lineFare, MIN_ADULT_AGE);
    }

    public static FareCalculator of(int lineFare, int age) {
        return new FareCalculator(lineFare, age);
    }

    public int calculate(int distance) {
        checkDistanceNotNegative(distance);
        return 0;
    }

    private void checkDistanceNotNegative(int distance) {
        if (distance < MIN_DISTANCE_OF_BASIC_FARE_SECTION) {
            throw new FareValidException(INVALID_OVER_DISTANCE);
        }
    }

}
