package nextstep.subway.line.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Distance {

    @Column(name = "distance")
    private int value;

    protected Distance() {
    }

    private Distance(int value) {
        Assert.isTrue(positive(value), "거리는 반드시 양수이어야 합니다.");
        this.value = value;
    }

    public static Distance from(int value) {
        return new Distance(value);
    }

    public static Distance from(double value) {
        return new Distance((int) value);
    }

    public int value() {
        return value;
    }

    public Distance sum(Distance distance) {
        return from(value + distance.value);
    }

    public Distance subtract(Distance distance) {
        return from(value - distance.value);
    }

    public boolean lessThanOrEqual(Distance distance) {
        return value <= distance.value;
    }

    public int ceilDivide(Distance distance) {
        return (int) Math.ceil((double) value / distance.value);
    }

    private boolean positive(int value) {
        return value > 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Distance distance = (Distance) o;
        return value == distance.value;
    }

    @Override
    public String toString() {
        return "Distance{" +
            "value=" + value +
            '}';
    }
}
