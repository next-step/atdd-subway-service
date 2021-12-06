package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    Integer value;

    public Distance(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public boolean isGraterOrEqual(Distance otherDistance) {
        return this.value > otherDistance.getValue();
    }

    public Distance add(Distance otherDistance) {
        return new Distance(this.value + otherDistance.getValue());
    }

    public Distance subtract(Distance otherDistance) {
        return new Distance(this.value - otherDistance.getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return Objects.equals(value, distance.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
