package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.Objects;

public class Fare {

    private static final BigDecimal BASE_RATE = BigDecimal.valueOf(1_250L);

    private static final BigDecimal EXTRA_RATE = BigDecimal.valueOf(100L);

    private final BigDecimal fare;

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
}
