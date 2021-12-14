package nextstep.subway.path.domain.fare;

import java.util.Objects;

public class Fare extends FareRule {

    private final int fare;

    public Fare() {
        this.fare = BASE_FARE;
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public Fare extraFare(int distance, int lineFare) {
        int distanceFare = distanceFare(distance);
        return new Fare(distanceFare + lineFare);
    }

    public Fare discount(int age) {
        return new Fare(discountFare(age, this));
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
