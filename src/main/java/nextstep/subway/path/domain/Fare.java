package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare implements Comparable<Fare> {

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(1_250L);
    private static final BigDecimal EXTRA_RATE = BigDecimal.valueOf(100L);
    private static final BigDecimal BASE_DISCOUNT_AMOUNT = BigDecimal.valueOf(350L);
    private static final BigDecimal BASE_DISCOUNT_RATE = BigDecimal.valueOf(1.0);
    private static final BigDecimal TEENAGER_DISCOUNT_RATE = BigDecimal.valueOf(0.2);
    private static final BigDecimal CHILDREN_DISCOUNT_RATE = BigDecimal.valueOf(0.5);

    @Column(name = "fare")
    private final BigDecimal fare;

    protected Fare() {
        this.fare = BASE_RATE;
    }

    public Fare(final BigDecimal fare) {
        this.fare = fare;
    }

    public static Fare of() {
        return new Fare(BASE_RATE);
    }

    public Fare calculateOverFare(final FareDistance distance) {
        final BigDecimal extraUnit = BigDecimal.valueOf(distance.calculateDistanceUnit());
        final BigDecimal overFare = EXTRA_RATE.multiply(extraUnit);
        return new Fare(fare.add(overFare));
    }

    public Fare applyDiscount(final Integer age) {
        BigDecimal discounted = fare;
        if (age >= 13 && age < 19) {
            final BigDecimal rate = BASE_DISCOUNT_RATE.subtract(TEENAGER_DISCOUNT_RATE);
            discounted = fare.subtract(BASE_DISCOUNT_AMOUNT).multiply(rate);
        }
        if (age >= 6 && age < 13) {
            final BigDecimal rate = BASE_DISCOUNT_RATE.subtract(CHILDREN_DISCOUNT_RATE);
            discounted = fare.subtract(BASE_DISCOUNT_AMOUNT).multiply(rate);
        }
        return new Fare(BigDecimal.valueOf(discounted.longValue()));
    }

    public Fare add(final Fare o) {
        final BigDecimal newFare = getFare().add(o.getFare());
        return new Fare(BigDecimal.valueOf(newFare.longValue()));
    }

    public BigDecimal getFare() {
        return fare;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Fare fare1 = (Fare) o;
        return Objects.equals(fare, fare1.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public int compareTo(final Fare o) {
        return fare.compareTo(o.fare);
    }
}
