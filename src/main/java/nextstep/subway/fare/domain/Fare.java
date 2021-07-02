package nextstep.subway.fare.domain;

import java.util.Objects;

public class Fare {

    public static final int BASE_FARE = 1250;
    public static final int ZERO_EXTRA_CHARGE = 0;
    public static final int DISTANCE_EXTRA_CHARGE = 100;
    public static final int DISTANCE_FIRST_INTERVAL_DIVIDER = 5;
    public static final int DISTANCE_SECOND_INTERVAL_DIVIDER = 8;

    private int fare;

    public Fare(int fare) {
        this.fare = fare;
    }

    public int getFare() {
        return fare;
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
