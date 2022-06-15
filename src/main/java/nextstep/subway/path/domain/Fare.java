package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;

import java.util.Optional;

import static nextstep.subway.path.domain.FarePolicy.BASIC_CHARGE;

public class Fare {
    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(Sections sections, int distance) {
        int fare = BASIC_CHARGE + sections.findOverFareOfLine() + calculateOverFare(distance);

        return new Fare(fare);
    }

    private static int calculateOverFare(int distance) {
        Optional<FarePolicy> farePolicy = FarePolicy.findFarePolicyByDistance(distance);

        return farePolicy
                .map(policy -> (int) ((Math.ceil((distance - 1) / policy.getDistanceStandardValue()) + 1) * policy.getOverFare()))
                .orElse(0);
    }

    public int getFare() {
        return fare;
    }
}
