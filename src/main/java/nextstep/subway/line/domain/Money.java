package nextstep.subway.line.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Money implements Comparable<Money> {

    public static final Money ZERO = Money.won(0);

    private final BigDecimal amount;

    public static Money won(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public Money minus(int amount) {
        return new Money(this.amount.subtract(BigDecimal.valueOf(amount)));
    }

    public Money multiply(BigDecimal value) {
        return new Money(this.amount.multiply(value));
    }

    public BigDecimal getMoney() {
        return amount;
    }

    public int toInt() {
        return amount.intValue();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof Money)) {
            return false;
        }

        Money money = (Money) object;
        return Objects.equals(amount.doubleValue(), money.amount.doubleValue());
    }


    public int hashCode() {
        return Objects.hashCode(amount);
    }

    @Override
    public int compareTo(Money o) {
        return this.amount.compareTo(o.amount);
    }

}
