package nextstep.subway.common.domain;

import io.jsonwebtoken.lang.Assert;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Fare implements Comparable<Fare> {

    private static final Fare ZERO = new Fare(0);

    private int value;

    protected Fare() {
    }

    private Fare(int value) {
        Assert.isTrue(positiveOrEqualZero(value), "요금은 반드시 0 또는 양수이어야 합니다.");
        this.value = value;
    }

    public static Fare zero() {
        return ZERO;
    }

    public static Fare from(int value) {
        return new Fare(value);
    }

    public int value() {
        return value;
    }

    public boolean greaterThan(Fare fare) {
        return value > fare.value;
    }

    public Fare sum(Fare fare) {
        return from(value + fare.value);
    }

    public Fare multiply(int target) {
        return from(this.value * target);
    }

    public Fare multiply(Percent percent) {
        return from(percent.percentageOf(this.value));
    }

    public Fare subtract(Fare fare) {
        return from(value - fare.value);
    }

    @Override
    public int compareTo(Fare other) {
        return Integer.compare(value, other.value);
    }

    private boolean positiveOrEqualZero(int value) {
        return value >= 0;
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
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public String toString() {
        return "Fare{" +
            "value=" + value +
            '}';
    }
}
