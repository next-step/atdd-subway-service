package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public class FareCalculator {
    private static final int DEFAULT_FARE = 1_250;
    private static final int EXTRA_FARE = 100;
    
    private static final int FARE_FIRST_RANGE_MIN = 10;
    private static final int FARE_FIRST_RANGE_MAX = 50;
    private static final int FARE_SECOND_RANGE_MIN = FARE_FIRST_RANGE_MAX;
    
    private static final int OVER_PER_DISTANCE_KM = 5;
    private static final int MAX_OVER_PER_DISTANCE_KM = 8;
    
    private static final int AGE_DISCOUNT_FARE = 350;
    private static final double CHILD_DISCOUNT_PERCENT = 0.5;
    private static final double YOUTH_DISCOUNT_PERCENT = 0.8;
    
    private static final int CHILD_MIN_AGE = 6;
    private static final int CHILD_MAX_AGE = 13;
    private static final int YOUTH_MIN_AGE = CHILD_MAX_AGE;
    private static final int YOUTH_MAX_AGE = 19;
    
    private FareCalculator() {
    }
    
    public static int calculator(Lines lines, List<Station> stations, int distance, int age) {
        return discountAgeFare(calculator(lines, stations, distance), age);
    }
    
    public static int calculator(Lines lines, List<Station> stations, int distance) {
        return calculator(distance) + lines.calculatorMaxSurcharge(stations);
    }
    
    public static int calculator(int distance) {
        if (distance > FARE_FIRST_RANGE_MIN && distance <= FARE_FIRST_RANGE_MAX) {
            return DEFAULT_FARE + calculateOverFare(distance - FARE_FIRST_RANGE_MIN, OVER_PER_DISTANCE_KM);
        }
        if (distance > FARE_SECOND_RANGE_MIN) {
            int firstRangeFare = calculateOverFare(FARE_SECOND_RANGE_MIN - FARE_FIRST_RANGE_MIN, OVER_PER_DISTANCE_KM);
            int secondRangeFare = calculateOverFare(distance - FARE_FIRST_RANGE_MAX, MAX_OVER_PER_DISTANCE_KM);
            return DEFAULT_FARE + firstRangeFare + secondRangeFare;
        }
        
        return DEFAULT_FARE;
    }
    
    private static int calculateOverFare(int distance, int overDistance) {
        return (int) ((Math.ceil((distance - 1) / overDistance) + 1) * EXTRA_FARE);
    }
    
    private static int discountAgeFare(int fare, int age) {
        if (age >= CHILD_MIN_AGE && age < CHILD_MAX_AGE) {
            return (int) ((fare - AGE_DISCOUNT_FARE) * CHILD_DISCOUNT_PERCENT);
        }
        if (age >= YOUTH_MIN_AGE && age < YOUTH_MAX_AGE) {
            return (int) ((fare - AGE_DISCOUNT_FARE) * YOUTH_DISCOUNT_PERCENT);
        }
        return fare;
    }
}
