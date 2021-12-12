package nextstep.subway.fare;

import java.util.*;

import javax.persistence.*;

@Embeddable
public class Fare {
    private static final String FARE_MUST_NOT_BE_NEGATIVE_EXCEPTION_STATEMENT = "요금은 음수가 될 수 없습니다.";

    private int fare;

    public Fare() {
    }

    private Fare(int fare) {
        validate(fare);
        this.fare = fare;
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    private void validate(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException(FARE_MUST_NOT_BE_NEGATIVE_EXCEPTION_STATEMENT);
        }
    }

    public Fare plus(Fare amount) {
        return new Fare(fare + amount.fare);
    }

    public Fare minus(Fare amount) {
        return new Fare(fare - amount.fare);
    }

    public Fare minus(int amount) {
        return new Fare(fare - amount);
    }

    public Fare multiply(Fare amount) {
        return new Fare(fare * amount.fare);
    }

    public Fare multiply(double amount) {
        return new Fare((int)(fare * amount));
    }

    public Fare round() {
        return new Fare((int)Math.round(fare));
    }

    public int fare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Fare fare1 = (Fare)o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }
}
