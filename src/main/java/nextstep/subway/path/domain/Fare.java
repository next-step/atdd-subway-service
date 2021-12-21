package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {
    private static final int DEFAULT_FARE = 1250;
    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    public static Fare plusFareFromDefaultFare(int additionalFare) {
        return new Fare(DEFAULT_FARE + additionalFare);
    }

    public Fare plus(Fare otherFare) {
        return new Fare(this.fare + otherFare.getFare());
    }

    public Fare plusDefaultFare() {
        return new Fare(this.fare + DEFAULT_FARE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
