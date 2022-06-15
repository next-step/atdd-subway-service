package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;

import java.util.Optional;

import static nextstep.subway.path.domain.FareDistancePolicy.BASIC_CHARGE;

public class Fare {
    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(Sections sections, TotalDistance totalDistance, int age) {
        return new Fare(calculateFare(sections, totalDistance, age));
    }

    private static int calculateFare(Sections sections, TotalDistance totalDistance, int age) {
        int fare = BASIC_CHARGE + sections.findOverFareOfLine() + calculateDistanceOverFare(totalDistance);
        return calculateAgeDiscountFare(fare, age);
    }

    private static int calculateDistanceOverFare(TotalDistance totalDistance) {
        return totalDistance.calculateOverFare();
    }

    private static int calculateAgeDiscountFare(int fare, int age) {
        Optional<FareAgePolicy> farePolicy = FareAgePolicy.findFarePolicyByAge(age);

        return farePolicy
                .map(fareAgePolicy -> (int) ((fare - fareAgePolicy.getDeduction()) * fareAgePolicy.getDiscountFare() + fareAgePolicy.getDeduction()))
                .orElse(fare);
    }

    public int getFare() {
        return fare;
    }
}
