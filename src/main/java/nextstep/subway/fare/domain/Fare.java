package nextstep.subway.fare.domain;

import nextstep.subway.exception.SubwayExceptionMessage;
import nextstep.subway.fare.policy.AgeDiscountCalculator;
import nextstep.subway.fare.policy.DistanceSurchargeCalculator;
import nextstep.subway.fare.policy.LineSurchargeCalculator;
import nextstep.subway.path.domain.Path;

public class Fare {

    private static final int BASIC_FARE = 1250;
    private Path shortestPath;
    private Integer age;
    private int calculatedFare;

    public Fare(Path shortestPath, Integer age) {
        validateShortestPathAndAge(shortestPath, age);
        this.shortestPath = shortestPath;
        this.age = age;
        calculateFare();
    }

    private void calculateFare() {
        int distanceSurcharge = new DistanceSurchargeCalculator().calculate(shortestPath);
        int lineSurcharge = new LineSurchargeCalculator().calculate(shortestPath);
        int totalFare = BASIC_FARE + distanceSurcharge + lineSurcharge;
        int discountByAge = new AgeDiscountCalculator().calculate(totalFare, age);
        this.calculatedFare = totalFare - discountByAge;
    }

    public int getCalculatedFare() {
        return calculatedFare;
    }

    private void validateShortestPathAndAge(Path shortestPath, Integer age) {
        if (shortestPath == null) {
            throw new IllegalArgumentException(SubwayExceptionMessage.EMPTY_SHORTEST_PATH.getMessage());
        }

        if (age < 0) {
            throw new IllegalArgumentException(SubwayExceptionMessage.INVALID_AGE.getMessage());
        }
    }

}
