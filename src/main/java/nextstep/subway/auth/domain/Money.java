package nextstep.subway.auth.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Money {

    private static final double ZERO = 0;

    private Double amount;

    protected Money() {
    }

    private Money(double amount) {
        this.amount = amount;
    }

    public static Money from(double amount) {
        return new Money(amount);
    }

    public static Money zero() {
        return new Money(ZERO);
    }

    public double getAmount() {
        return this.amount;
    }

    protected void setAmount(double amount) {
        this.amount = amount;
    }

    public Money plus(Money targetMoney) {
        return Money.from(this.amount + targetMoney.amount);
    }

    public Money minus(Money targetMoney) {
        if (this.amount < targetMoney.amount) {
            throw new IllegalArgumentException(
                    "기존 요금은 차감하고자 하는 요금보다 커야합니다. 기존요금 " + this.amount + " 차감요금:" + targetMoney.amount);
        }
        return Money.from(this.amount - targetMoney.amount);
    }

    public Money divideByDecimalPoint(double rate) {
        return Money.from(this.amount - (this.amount * rate));
    }

    public Money mul(double target) {
        return Money.from(this.amount * target);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Money money = (Money) o;
        return Double.compare(money.amount, amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
