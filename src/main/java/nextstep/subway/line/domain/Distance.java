package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {
    @Column(name = "distance")
    private int value;

    public Distance(final int value) {
        this.value = value;
    }

    protected Distance() {
    }

    int value() {
        return this.value;
    }

    public boolean isSmallOrEqualTo(final Distance distance) {
        return this.value <= distance.value;
    }

    public void minus(final Distance distance) {
        this.value -= distance.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
