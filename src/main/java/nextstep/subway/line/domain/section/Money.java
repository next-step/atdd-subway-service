package nextstep.subway.line.domain.section;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
public class Money {
    private static final BigDecimal DEFAULT_ADD_FARE = BigDecimal.ZERO;

    private BigDecimal money;

    protected Money() {}

    private Money(BigDecimal money) {
        this.money = money;
    }

    public static Money from(BigDecimal money) {
        return new Money(money);
    }

    public static Money ofZero() {
        return new Money(DEFAULT_ADD_FARE);
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void addMoney(Money money) {
        this.money.add(money.money);
    }

    public void subtractMoney(Money money) {
        this.money.subtract(money.money);
    }

    public void multiplyMoney(BigDecimal money) {
        this.money.multiply(money);
    }
}
