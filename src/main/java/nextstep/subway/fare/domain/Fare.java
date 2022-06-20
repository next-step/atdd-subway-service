package nextstep.subway.fare.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class Fare implements Comparable<Fare> {
    private final BigDecimal value;
    @Transient
    private final AgeFarePolicy ageFarePolicy = new AgeFarePolicy();

    protected Fare() {
        this.value = BigDecimal.ZERO;
    }

    private Fare(BigDecimal value) {
        validate(value);
        this.value = value.setScale(0, RoundingMode.CEILING);
    }

    public static Fare from(double value) {
        return new Fare(BigDecimal.valueOf(value));
    }

    public Fare add(Fare target) {
        return new Fare(value.add(target.value));
    }

    public Fare minus(Fare target) {
        validateMinus(target);
        return new Fare(value.subtract(target.value));
    }


    public Fare getRateFare(Double rate) {
        return new Fare(value.multiply(BigDecimal.valueOf(rate / 100)));
    }

    public Fare discount(Integer age) {
        return ageFarePolicy.discount(this, age);
    }

    public BigDecimal getValue() {
        return value;
    }

    private void validate(BigDecimal value) {
        if (value.compareTo(BigDecimal.ZERO) == -1) {
            throw new IllegalArgumentException("운임요금은 0보다 작을 수 없습니다.");
        }
    }

    private void validateMinus(Fare target) {
        if (value.compareTo(target.value) == -1) {
            throw new IllegalArgumentException("운임요금은 더 큰 요금으로 뺄 수 없습니다.");
        }
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
