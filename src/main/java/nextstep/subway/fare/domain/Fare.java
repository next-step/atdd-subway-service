package nextstep.subway.fare.domain;

import nextstep.subway.exception.SubwayExceptionMessage;
import nextstep.subway.path.domain.Path;
import nextstep.subway.path.domain.SectionEdge;

import java.util.Comparator;
import java.util.NoSuchElementException;

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
        int distanceSurcharge = calculateDistanceSurcharge(shortestPath.getDistance());
        int lineSurcharge = calculateLineSurcharge(shortestPath);
        int totalFare = BASIC_FARE + distanceSurcharge + lineSurcharge;
        int discountByAge = calculateAgeDiscount(totalFare, age);
        this.calculatedFare = totalFare - discountByAge;
    }

    private int calculateLineSurcharge(Path shortestPath) {
        return shortestPath.getSectionEdges().stream()
                .max(Comparator.comparing(SectionEdge::getLineSurcharge))
                .orElseThrow(NoSuchElementException::new)
                .getLineSurcharge();
    }

    private int calculateAgeDiscount(int totalFare, Integer age) {
        if (age >= 13 && age < 19) {
            return (int) ((totalFare - 350) * 0.2);
        }

        if (age >= 6 && age < 13) {
            return (int) ((totalFare - 350) * 0.5);
        }

        return 0;
    }

    private int calculateDistanceSurcharge(int distance) {
        int distanceSurcharge = 0;
        if (distance > 50) {
            int extraDistance = distance - 50;
            distanceSurcharge += (Math.ceil((double) extraDistance / 8) * 100);
            distance -= extraDistance;
        }

        if (distance <= 50 && distance >= 10) {
            int extraDistance = distance - 10;
            distanceSurcharge += (Math.ceil((double) extraDistance / 5) * 100);
        }

        return distanceSurcharge;
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
