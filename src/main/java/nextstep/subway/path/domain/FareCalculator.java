package nextstep.subway.path.domain;

import java.util.List;

import nextstep.subway.line.domain.Lines;
import nextstep.subway.station.domain.Station;

public class FareCalculator {
    private FareCalculator() {
    }
    
    public static int calculator(Lines lines, List<Station> stations, int distance, int age) {
        return AgeDiscountPolicy.discount(calculator(lines, stations, distance), age);
    }
    
    public static int calculator(Lines lines, List<Station> stations, int distance) {
        return DistanceFarePolicy.calculator(distance) + lines.calculatorMaxSurcharge(stations);
    }
    
    static int calculator(int distance) {
        return DistanceFarePolicy.calculator(distance);
    }
    
}
