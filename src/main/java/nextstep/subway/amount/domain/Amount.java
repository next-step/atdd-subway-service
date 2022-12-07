package nextstep.subway.amount.domain;

import java.util.Objects;

public class Amount {
    private final long amount;

    private Amount(long amount) {
        this.amount = amount;
    }

    public static Amount from(long amount) {
        return new Amount(amount);
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
