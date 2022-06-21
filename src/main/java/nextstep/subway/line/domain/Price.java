package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Price implements Comparable<Price> {
    @Transient
    private static final long MIN_VALUE = 0;
    @Column(name = "extra_charge", nullable = false)
    private final long money;

    public Price() {
        this(0);
    }

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

    public long of() {
        return this.money;
    }

    private Price minusBy(final long source) {
        return new Price(source - money);
    }

    private Price plus(final long addMoney) {
        return new Price(money + addMoney);
    }

    private int compareTo(final long target) {
        return  Long.compare(this.money, target);
    }

    @Override
    public int compareTo(Price price) {
        return price.compareTo(this.money);
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
