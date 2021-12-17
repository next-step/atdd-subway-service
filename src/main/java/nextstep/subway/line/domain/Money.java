package nextstep.subway.line.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Money {
    private int money;

    protected Money() {
    }

    public Money(int money) {
        this.money = money;
    }

    public Money plus(int value) {
        return new Money(this.money + value);
    }

    public Money minus(int value) {
        return new Money(money - value);
    }

    public Money multiply(float value) {
        float result = money * value;
        return new Money(Math.round(result));
    }

    private void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Money money1 = (Money) o;
        return money == money1.money;
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
