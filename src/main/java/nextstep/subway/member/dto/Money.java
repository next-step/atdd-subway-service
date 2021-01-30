package nextstep.subway.member.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    private BigDecimal value;

    private Money(BigDecimal value) {
        this.value = value;
    }

    public static Money of(long i) {
        return new Money(BigDecimal.valueOf(i));
    }

    public long getValue() {
        return value.longValue();
    }

    public Money multiply(double value) {
        return new Money(this.value.multiply(BigDecimal.valueOf(value)).setScale(0, BigDecimal.ROUND_DOWN));
    }

    public Money subtract(Money discount) {
        return new Money(this.value.subtract(discount.value));
    }


    public Money add(Money value) {
        return new Money(this.value.add(value.value));
    }

    @Override
    public String toString() {
        return "Money{" +
                "value=" + value +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
