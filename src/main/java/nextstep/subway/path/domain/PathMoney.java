package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.Money;

import java.util.Objects;

public class PathMoney {

    private Money money;

    private PathMoney(Money money) {
        this.money = money;
    }

    public static PathMoney from(Money money) {
        return new PathMoney(money);
    }

    public PathMoney mul(int count) {
        return PathMoney.from(this.money.mul(count));
    }

    public Money getCharge() {
        return this.money;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PathMoney pathMoney = (PathMoney) o;
        return Objects.equals(money, pathMoney.money);
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }
}
