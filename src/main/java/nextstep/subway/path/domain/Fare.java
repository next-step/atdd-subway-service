package nextstep.subway.path.domain;

import java.util.Objects;

public class Fare {

    private long fare;

    public Fare(long fare) {
        this.fare = fare;
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
}
