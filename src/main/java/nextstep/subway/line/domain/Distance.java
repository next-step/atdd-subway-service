package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Distance {

    public static final int DISTANCE_UNIT_LEVEL1 = 5;
    public static final int DISTANCE_UNIT_LEVEL2 = 8;
    private static final int ZERO = 0;

    @Column(name = "distance")
    private int value;

    protected Distance() {
        this(ZERO);
    }

    public Distance(final int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

    public boolean isSmallOrEqualTo(final Distance distance) {
        return this.value <= distance.value;
    }

    public void minus(final Distance distance) {
        this.value -= distance.value;
    }

    public boolean isBiggerThan(final int value) {
        return this.value > value;
    }

    public double minus(final int value) {
        return this.value - value;
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
