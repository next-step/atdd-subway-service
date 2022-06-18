package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Sections;
import nextstep.subway.member.domain.MemberAge;

import static nextstep.subway.path.domain.FareDistancePolicy.BASIC_CHARGE;

public class Fare {
    private final int fare;

    private Fare(int fare) {
        this.fare = fare;
    }

    public static Fare of(Sections sections, TotalDistance totalDistance, MemberAge age) {
        return new Fare(calculateFare(sections, totalDistance, age));
    }

    private static int calculateFare(Sections sections, TotalDistance totalDistance, MemberAge age) {
        int resultFare = BASIC_CHARGE + sections.findOverFareOfLine() + calculateDistanceOverFare(totalDistance);
        return FareAgePolicy.calculateDiscountFare(resultFare, age.findAge());
    }

    private static int calculateDistanceOverFare(TotalDistance totalDistance) {
        return totalDistance.calculateOverFare();
    }

    public int getFare() {
        return fare;
    }
}
