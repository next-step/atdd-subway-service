package nextstep.subway.fare.domain;

import nextstep.subway.fare.exception.BelowZeroIntegerException;
import nextstep.subway.fare.exception.LessZeroOperationException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;

@Embeddable
public class Fare {
    public static final String NAME = "요금";
    public static final Fare DEFAULT_FARE = new Fare(1250);

    @Column(name = "fare")
    private BigDecimal value = new BigDecimal(0);

    protected Fare(){
    }

    public Fare(int value) {
        validateConstructor(value);
        this.value = valueOf(value);
    }

    public Fare(BigDecimal value) {
        validateConstructor(value);
        this.value = value;
    }

    public Fare add(Fare plusedFare) {
        return new Fare(this.value.add(plusedFare.value));
    }

    public Fare subtract(Fare substractedFare) {
        validateLowerFare(substractedFare);
        return new Fare(this.value.subtract(substractedFare.value));
    }

    public Fare multiply(Rate multipliedRate) {
        return new Fare(this.value.multiply(multipliedRate.getValue()));
    }

    public int getValue() {
        return value.intValueExact();
    }

    public int compareTo(Fare target) {
        return this.value.compareTo(target.value);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
                "value=" + value +
                '}';
    }

    private void validateLowerFare(Fare fare) {
        if (this.value.compareTo(fare.value) < 0) {
            throw new LessZeroOperationException(NAME);
        }
    }

    private void validateConstructor(BigDecimal value) {
        if (value.compareTo(ZERO) < 0) {
            throw new LessZeroOperationException(NAME);
        }
    }

    private void validateConstructor(int value) {
        if (value < 0) {
            throw new BelowZeroIntegerException(NAME);
        }
    }
}
