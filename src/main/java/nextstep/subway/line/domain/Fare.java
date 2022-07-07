package nextstep.subway.line.domain;

import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {
    public static final Fare ZERO = new Fare();

    private int value;

    protected Fare() {
        this(0);
    }

    private Fare(int value) {
        validate(value);
        this.value = value;
    }

    public void validate(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("요금은 0이상만 가능합니다.");
        }
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    public int value() {
        return value;
    }

    @Override
    public String toString() {
        return "Fare{" + "value=" + value + '}';
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
    public int hashCode() {
        return Objects.hash(value);
    }
}
