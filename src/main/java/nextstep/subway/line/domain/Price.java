package nextstep.subway.line.domain;

import java.util.Objects;

public class Price {
    private static final long MIN_VALUE = 0;
    private final long money;

    public Price(final long money) {
        if (money < MIN_VALUE) {
            throw new IllegalArgumentException("음수를 가질수 없습니다.");
        }
        this.money = money;
    }

    public Price plus(final Price target) {
        return target.plus(money);
    }

    public Price minus(final Price target) {
        return target.minusBy(money);
    }

    private Price minusBy(final long source) {
        return new Price(source - money);
    }

    private Price plus(final long addMoney) {
        return new Price(money + addMoney);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return money == price.money;
    }

    @Override
    public int hashCode() {
        return Objects.hash(money);
    }

    @Override
    public String toString() {
        return "Price{" +
                "money=" + money +
                '}';
    }
}
