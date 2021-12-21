package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Fare {
    @Column
    private BigDecimal fare;

    protected Fare() {
    }

    private Fare(final BigDecimal fare) {
        this.fare = fare;
    }

    public static Fare from(final BigDecimal fare) {
        return new Fare(fare);
    }

    public static Fare from(final Long fare) {
        return new Fare(new BigDecimal(fare));
    }

    public static Fare from(final int fare) {
        return new Fare(new BigDecimal(fare));
    }

    public BigDecimal value() {
        return fare;
    }

    public Fare minus(final Fare deduction) {
        return new Fare(this.fare.subtract(deduction.fare));
    }

    public Fare multiply(final Fare discountFare) {
        final BigDecimal amount = this.fare.multiply(discountFare.fare);
        return new Fare(amount.stripTrailingZeros());
    }

    public Fare plus(final Fare fare) {
        return new Fare(this.fare.add(fare.fare));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return Objects.equals(fare, fare1.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
