package nextstep.subway.fare.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
public class Fare implements Comparable<Fare> {
    private final BigDecimal value;

    protected Fare() {
        this.value = BigDecimal.ZERO;
    }

    private Fare(BigDecimal value) {
        this.value = value;
    }

    public static Fare from(double value) {
        return new Fare(BigDecimal.valueOf(value));
    }

    public Fare add(Fare target) {
        return new Fare(value.add(target.value));
    }

    public BigDecimal getValue() {
        return value;
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
        return Objects.equals(value, fare.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value.toString() +
                '}';
    }

    @Override
    public int compareTo(Fare target) {
        return value.compareTo(target.value);
    }
}
