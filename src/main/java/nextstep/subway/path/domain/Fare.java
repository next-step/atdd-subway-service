package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {

    private double fare;

    protected Fare() {
        this.fare = 0;
    }

    public Fare(double fare) {
        this.fare = fare;
    }

    public double getFare() {
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
        return Double.compare(fare1.fare, fare) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
