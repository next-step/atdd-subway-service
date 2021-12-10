package nextstep.subway.line.domain;

import java.util.Arrays;
import java.util.Objects;

public class Fare {

    public static final Fare defaultFare = new Fare(1250L);
    public static final Fare NOT_BENEFIT = new Fare(0L);

    private long fare;

    public static Fare sum(Fare ... fares) {
        return Arrays.stream(fares)
            .reduce(new Fare(0L), Fare::add);
    }

    public static Fare ceil(Distance distance){
        long ceil = (long) Math.ceil(distance.getDistance());
        return new Fare(ceil);
    }

    public Fare(long fare) {
        this.fare = fare;
    }

    public Fare add(Fare policyFare) {
        final long result = this.fare + policyFare.fare;
        return new Fare(result);
    }

    public long longValue() {
        return fare;
    }

    public Fare subtract(Fare subtractFare) {
        final long result = this.fare - subtractFare.fare;
        return new Fare(result);
    }

    public Fare add(int val) {
        final long result = this.fare + val;
        return new Fare(result);
    }

    public Fare multiply(Fare multiply) {
        final long result = this.fare * multiply.fare;
        return new Fare(result);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare1 = (Fare) o;
        return Objects.equals(fare, fare1.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public String toString() {
        return String.valueOf(fare);
    }
}
