package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Fare {

    public static final int SCALE = 0;
    public static final Fare ZERO = Fare.valueOf(0);

    private final BigDecimal value;

    private Fare(BigDecimal value) {
        this.value = value.setScale(SCALE, RoundingMode.FLOOR);
    }

    public static Fare valueOf(int value) {
        return new Fare(BigDecimal.valueOf(value));
    }

    public Fare add(Fare addend) {
        return new Fare(value.add(addend.value));
    }

    public Fare multiply(int multiplicand) {
        return new Fare(value.multiply(BigDecimal.valueOf(multiplicand)));
    }

    public boolean isGreaterThan(Fare other) {
        return this.value.compareTo(other.value) > 0;
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
        return value.equals(fare.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Fare{" +
                "value=" + value +
                '}';
    }
}
