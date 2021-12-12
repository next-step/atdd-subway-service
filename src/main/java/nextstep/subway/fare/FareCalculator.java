package nextstep.subway.fare;

import java.util.*;

import nextstep.subway.line.domain.*;

public class FareCalculator {
    private final int distance;
    private final List<Line> lines;
    private final int age;

    private FareCalculator(int distance, List<Line> lines, int age) {
        this.age = age;
        this.lines = lines;
        this.distance = distance;
    }

    public static FareCalculator from(int distance, List<Line> lines, int age) {
        return new FareCalculator(distance, lines, age);
    }

    public Fare totalFare() {
        Fare fareByDistance = calculateFareByDistance();
        Fare extraFareByLine = calculateExtraFareByLine();
        return calculateFareByAge(fareByDistance.plus(extraFareByLine));
    }

    private Fare calculateFareByDistance() {
        return FareByDistancePolicy.calculateFare(distance);
    }

    private Fare calculateFareByAge(Fare fare) {
        return FareByAgePolicy.calculateFare(age, fare);
    }

    private Fare calculateExtraFareByLine() {
        int extraFareByLine = lines.stream()
            .mapToInt(Line::getExtraFare)
            .max()
            .orElse(0);
        return Fare.from(extraFareByLine);
    }


}
