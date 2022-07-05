package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    private long fare;

    private Fare(long fare) {
        this.fare = fare;
    }

    public static Fare of(long fare) {
        return new Fare(fare);
    }

    protected Fare() {

    }

    public long value() {
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
