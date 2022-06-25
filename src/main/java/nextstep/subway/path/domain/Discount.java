package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Charge;

import java.util.Objects;

public class Discount {
    private static final long MIN = 0;
    private static final long MAX = 100;
    private final long discount;

    public Discount() {
        this(MIN);
    }

    public Discount(final long discount) {
        if (discount < MIN) {
            throw new IllegalArgumentException("음수를 가질수 없습니다");
        }
        this.discount = discount;
    }

    public Discount plus(final Discount source) {
        return source.plus(discount);
    }

    public Discount minus(final Discount target) {
        return target.minusBy(discount);
    }

    public Charge calculate(final Charge charge) {
        return new Charge(charge.value() * (MAX - discount) / MAX);
    }

    private Discount plus(final long source) {
        return new Discount(source + discount);
    }

    private Discount minusBy(final long source) {
        return new Discount(source - discount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Discount discount1 = (Discount) o;
        return discount == discount1.discount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(discount);
    }
}
