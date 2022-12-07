package nextstep.subway.fare.domain;

import nextstep.subway.path.domain.Path;

public class Fare {
    public static final int BASIC_FARE = 1250;

    private int fare;

    private Fare(int distance, int additionalFare, int age) {
        fare = calculate(distance, additionalFare, age);
    }

    public static Fare calculateFare(int distance, int additionalFare, int age) {
        return new Fare(distance, additionalFare, age);
    }

    public static Fare calculateFare(Path path, int age) {
        return new Fare(path.getDistance(), path.getAdditionalFareByLine(), age);
    }

    private int calculate(int distance, int additionalFare, int age){
        int fare = subtractionByAge(age);
        fare += additionByDistance(distance);
        fare += additionByLine(additionalFare);
        return fare;
    }

    private int additionByLine(int additionalFare){
        return additionalFare;
    }

    private int additionByDistance(int distance){
        return FareTypeByDistance.calculate(distance);
    }

    private int subtractionByAge(int age){
        return FareTypeByAge.calculate(age, BASIC_FARE);
    }

    public int getFare() {
        return fare;
    }
}
