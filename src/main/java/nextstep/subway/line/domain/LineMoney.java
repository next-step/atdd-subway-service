package nextstep.subway.line.domain;

import nextstep.subway.auth.domain.Money;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;

@Embeddable
public class LineMoney {

    @Embedded
    private Money money;

    private LineMoney(Money money) {
        this.money = money;
    }

    public static LineMoney from(Money money) {
        return new LineMoney(money);
    }

    protected LineMoney() {

    }

    public Money getMoney() {
        return money;
    }

    public void setMoney(Money money) {
        this.money = money;
    }
}
