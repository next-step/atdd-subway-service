package nextstep.subway.path.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare implements Comparable<Fare> {

    private static final int BASE_AMOUNT = 1_250;
    private static final int EXTRA_AMOUNT = 100;
    private static final int BASE_DISCOUNT_AMOUNT = 350;
    private static final double BASE_DISCOUNT_RATE = 1.0;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

    @Column(name = "fare")
    private final int fare;

    protected Fare() {
        this.fare = BASE_AMOUNT;
    }

    public Fare(final int fare) {
        this.fare = fare;
    }

    public static Fare of() {
        return new Fare(BASE_AMOUNT);
    }

    public Fare calculateOverFare(final FareDistance distance) {
        final int extraUnit = distance.calculateDistanceUnit();
        final int overFare = EXTRA_AMOUNT * extraUnit;
        return new Fare(fare + overFare);
    }

    public Fare applyDiscount(final Integer age) {
        int discounted = fare;
        if (age >= 13 && age < 19) {
            final double rate = BASE_DISCOUNT_RATE - TEENAGER_DISCOUNT_RATE;
            discounted = (int) ((fare - BASE_DISCOUNT_AMOUNT) * rate);
        }
        if (age >= 6 && age < 13) {
            final double rate = BASE_DISCOUNT_RATE - CHILDREN_DISCOUNT_RATE;
            discounted = (int) ((fare - BASE_DISCOUNT_AMOUNT) * rate);
        }
        return new Fare(discounted);
    }

    public Fare add(final Fare o) {
        return new Fare(getFare() + o.getFare());
    }

    public int getFare() {
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
        return Integer.compare(getFare(), o.getFare());
    }
}
