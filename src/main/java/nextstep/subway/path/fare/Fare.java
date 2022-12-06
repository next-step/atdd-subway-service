package nextstep.subway.path.fare;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
public class Fare {

    private static final int SCALE = 0;
    private static final int MULTIPLY_SCALE = 2;
    public static final Fare ZERO = Fare.valueOf(0);

    @Column(nullable = false)
    private BigDecimal value;

    protected Fare() {
    }

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

    public int compare(Fare other) {
        return value.compareTo(other.value);
    }

    public int toInt() {
        return value.intValue();
    }

    public Fare minus(Fare subtract) {
        return new Fare(value.subtract(subtract.value));
    }

    private Fare multiply(BigDecimal multiplicand) {
        return new Fare(value.multiply(multiplicand.setScale(MULTIPLY_SCALE, RoundingMode.FLOOR)));
    }

    public Fare multiply(double multiplicand) {
        return multiply(BigDecimal.valueOf(multiplicand));
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
