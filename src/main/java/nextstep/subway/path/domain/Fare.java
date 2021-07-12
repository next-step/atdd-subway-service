package nextstep.subway.path.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    private long fare;

    public Fare() {
    }

    public Fare(long fare) {
        this.fare = fare;
    }

    public static int compare(Fare source, Fare target) {
        return (source.fare < target.fare) ? -1 : ((source == target) ? 0 : 1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Fare fare1 = (Fare)o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    public Fare add(Fare extraFare) {
        return new Fare(this.fare + extraFare.fare);
    }
}
