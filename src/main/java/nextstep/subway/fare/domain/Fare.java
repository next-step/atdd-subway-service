package nextstep.subway.fare.domain;

import nextstep.subway.path.domain.Path;

public class Fare {
    public static final int BASIC_FARE = 1250;

    private int fare;

    public Fare(int distance, int additionalFare, int age){
        fare = calculate(distance, additionalFare, age);
    }

    public Fare(Path path, int age) {
        fare = calculate(path.getDistance(), path.getAdditionalFareByLine(), age);
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
