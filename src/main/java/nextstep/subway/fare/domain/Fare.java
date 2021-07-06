package nextstep.subway.fare.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare {

    public static final int ZERO_EXTRA_CHARGE = 0;
    public static final int DISTANCE_EXTRA_CHARGE_UNIT = 100;
    public static final int DISTANCE_FIRST_INTERVAL_DIVIDER = 5;
    public static final int DISTANCE_SECOND_INTERVAL_DIVIDER = 8;
    public static final int AGE_DISCOUNT_DEDUCTION_FARE = 350;
    public static final double AGE_TEENAGER_DISCOUNT_RATE = 0.2;
    public static final double AGE_CHILD_DISCOUNT_RATE = 0.5;

    @Column
    private int fare;

    public Fare() {
    }

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
    }

    public Fare plus(Fare otherFare) {
        return new Fare(fare + otherFare.fare);
    }

    public Fare multiply(int count) {
        return new Fare(fare * count);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare other = (Fare) o;
        return fare == other.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

}
