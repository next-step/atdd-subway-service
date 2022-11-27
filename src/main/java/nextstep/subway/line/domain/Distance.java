package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    @Column(name = "distance")
    private int value;

    protected Distance() {
        this.value = 0;
    }

    public Distance(int value) {
        this.value = value;
    }

    public static Distance sum(Distance distance1, Distance distance2) {
        return new Distance(distance1.value + distance2.value);
    }

    public int getDistance() {
        return value;
    }

    public boolean isShortOrEqualTo(Distance distance) {
        return this.value <= distance.value;
    }

    public void minus(Distance distance) {
        this.value -= distance.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
