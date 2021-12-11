package nextstep.subway.line.domain.fare;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {

    public static final Money ZERO = Money.wons(0);

    private final BigDecimal amount;

    public static Money wons(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    private Money(BigDecimal amount) {
        this.amount = amount;
    }

    public Money plus(Money amount) {
        return new Money(this.amount.add(amount.amount));
    }

    public BigDecimal getMoney() {
        return amount;
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
}
