package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Surcharge;
import nextstep.subway.policy.AgeType;
import nextstep.subway.policy.DistanceType;

import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE = 1_250;

    private final int fare;

    public Fare(final int fare) {
        this.fare = fare;
    }

    public static Fare ofByDistance(final int distance, final Surcharge surcharge) {
        DistanceType distanceType = DistanceType.calculatorDistanceType(distance);
        int additionalFare = distanceType.calculatorAdditionalFare(distance);
        return new Fare(DEFAULT_FARE + additionalFare + surcharge.getSurcharge());
    }

    public Fare discountByAge(final AgeType ageType) {
        return new Fare(ageType.discount(fare));
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return this.fare == fare.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
