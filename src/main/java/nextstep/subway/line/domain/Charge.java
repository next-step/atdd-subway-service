package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Transient;
import java.util.Objects;

@Embeddable
public class Charge implements Comparable<Charge> {
    @Transient
    private static final long MIN_VALUE = 0;
    @Column(name = "extra_charge", nullable = false)
    private final long charge;

    public Charge() {
        this(0);
    }

    public Charge(final long charge) {
        if (charge < MIN_VALUE) {
            throw new IllegalArgumentException("음수를 가질수 없습니다.");
        }
        this.charge = charge;
    }

    public Charge plus(final Charge target) {
        return target.plus(charge);
    }

    public Charge minus(final Charge target) {
        return target.minusBy(charge);
    }

    public long of() {
        return this.charge;
    }

    private Charge minusBy(final long source) {
        return new Charge(source - charge);
    }

    private Charge plus(final long addMoney) {
        return new Charge(charge + addMoney);
    }

    private int compareTo(final long target) {
        return  Long.compare(this.charge, target);
    }

    @Override
    public int compareTo(Charge charge) {
        return charge.compareTo(this.charge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Charge charge = (Charge) o;
        return this.charge == charge.charge;
    }

    @Override
    public int hashCode() {
        return Objects.hash(charge);
    }
}
