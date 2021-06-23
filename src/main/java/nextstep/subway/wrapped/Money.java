package nextstep.subway.wrapped;

import java.util.Objects;

import static java.lang.String.format;

public class Money {
    private static final int MINIMUM_MONEY = 0;

    private int money;

    public Money(int money) {
        validate(money);

        this.money = money;
    }

    private void validate(int money) {
        if (money < MINIMUM_MONEY) {
            throw new IllegalArgumentException(format("돈은 %d원 이상이여야 합니다.", MINIMUM_MONEY));
        }
    }

    public Money plus(Money money) {
        return new Money(this.money + money.money);
    }

    public Money minus(Money money) {
        return new Money(this.money - money.money);
    }

    public Money multi(Money money) {
        return new Money(this.money * money.money);
    }

    public Money divide(Money money) {
        return new Money(this.money / money.money);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money1 = (Money) o;
        return money == money1.money;
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
