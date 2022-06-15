package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;

import java.util.Optional;

import static nextstep.subway.path.domain.FareDistancePolicy.BASIC_CHARGE;

public class Fare {
    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(Sections sections, int distance, int age) {
        return new Fare(calculateFare(sections, distance, age));
    }

    private static int calculateFare(Sections sections, int distance, int age) {
        int fare = BASIC_CHARGE + sections.findOverFareOfLine() + calculateDistanceOverFare(distance);
        return calculateAgeDiscountFare(fare, age);
    }

    private static int calculateDistanceOverFare(int distance) {
        Optional<FareDistancePolicy> farePolicy = FareDistancePolicy.findFarePolicyByDistance(distance);

        return farePolicy
                .map(policy -> (int) ((Math.ceil((distance - 1) / policy.getDistanceStandardValue()) + 1) * policy.getOverFare()))
                .orElse(0);
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
