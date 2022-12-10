package nextstep.subway.fare.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Fare implements Comparable<Fare> {

    public static final Fare DEFAULT_FARE = new Fare(1250);

    private int fare;

    protected Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    @Override
    public int compareTo(Fare otherFare) {
        return Integer.compare(this.fare, otherFare.fare);
    }

    public Fare plus(Fare other) {
        return new Fare(this.fare + other.fare);
    }

    public Fare minus(Fare other) {
        return new Fare(this.fare - other.fare);
    }

    public Fare discount(int percent) {
        return this.minus(new Fare(disCountPrice(percent)));
    }

    private int disCountPrice(int percent) {
        return fare * percent / 100;
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

    @Override
    public String toString() {
        return "Fare{" +
            "fare=" + fare +
            '}';
    }
}
