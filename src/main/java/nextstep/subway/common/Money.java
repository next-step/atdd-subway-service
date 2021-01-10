package nextstep.subway.common;

import java.util.Objects;

public class Money implements Comparable<Money> {

    private static final int MIN_VALUE = 0;

    private final int amount;

    private Money(final int amount) {
        if (amount < MIN_VALUE) {
            throw new IllegalArgumentException("금액은 음수를 허용하지 않습니다.");
        }
        this.amount = amount;
    }

    public static Money valueOf(final int value) {
        return new Money(value);
    }

    public static Money zero() {
        return valueOf(MIN_VALUE);
    }

    public Money add(final Money other) {
        return new Money(amount + other.amount);
    }

    public Money subtract(final Money other) {
        return new Money(amount - other.amount);
    }

    public Money multiply(final double value) {
        double multiply = amount * value;
        return new Money((int) multiply);
    }

    public int getAmount() {
        return amount;
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

    @Override
    public int compareTo(final Money other) {
        return Integer.compare(amount, other.amount);
    }
}
