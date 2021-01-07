package nextstep.subway.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Money {

    private static final BigDecimal MIN_VALUE = BigDecimal.ZERO;

    private final BigDecimal amount;

    private Money(final BigDecimal amount) {
        if (isNegative(amount)) {
            throw new IllegalArgumentException("금액은 음수를 허용하지 않습니다.");
        }
        this.amount = amount;
    }

    private boolean isNegative(final BigDecimal amount) {
        return amount.compareTo(MIN_VALUE) < 0;
    }

    public static Money valueOf(final int value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public Money add(final Money other) {
        return new Money(amount.add(other.amount));
    }

    public Money subtract(final Money other) {
        return new Money(amount.subtract(other.amount));
    }

    public Money multiply(final double value) {
        BigDecimal multiply = amount.multiply(new BigDecimal(value))
                .setScale(0, RoundingMode.FLOOR);
        return new Money(multiply);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        final Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
