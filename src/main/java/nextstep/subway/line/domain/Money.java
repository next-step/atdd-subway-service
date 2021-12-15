package nextstep.subway.line.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Money {
    private int money;

    protected Money() {
    }

    public Money(int money) {
        this.money = money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        return money;
    }
}
