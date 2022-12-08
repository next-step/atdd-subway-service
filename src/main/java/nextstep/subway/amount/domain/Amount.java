package nextstep.subway.amount.domain;

import java.util.Objects;

import javax.persistence.Embeddable;

@Embeddable
public class Amount {
    private long amount;

    protected Amount() {
    }

    private Amount(long amount) {
        this.amount = amount;
    }

    public static Amount from(long amount) {
        return new Amount(amount);
    }

    public static Amount empty() {
        return new Amount(0L);
    }

    public Amount sum(Amount other) {
        return new Amount(this.amount + other.amount);
    }

    public long value() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Amount amount1 = (Amount)o;
        return amount == amount1.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
