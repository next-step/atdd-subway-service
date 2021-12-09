package nextstep.subway.line.dto;

import nextstep.subway.line.domain.Surcharge;

import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE = 1_250;

    private final int Fare;

    public Fare(final int fare) {
        this.Fare = fare;
    }

    public static Fare ofByDistance(final int distance, final Surcharge surcharge) {
        DistanceType distanceType = DistanceType.calculatorDistanceType(distance);
        int additionalFare = distanceType.calculatorAdditionalFare(distance);
        return new Fare(DEFAULT_FARE + additionalFare + surcharge.getSurcharge());
    }

    public int getFare() {
        return Fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return Fare == fare.Fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Fare);
    }
}
