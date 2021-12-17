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
    
    private FareCalculator() {
    }
    
    public static int calculator(Lines lines, List<Station> stations, int distance, int age) {
        return AgeDiscountPolicy.discount(calculator(lines, stations, distance), age);
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
}
