package nextstep.subway.fare.domain;

import nextstep.subway.fare.application.InvalidFareException;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
@Access(AccessType.FIELD)
public class Fare implements Comparable<Fare> {
    private static final int MIN = 0;
    private final int value;

    protected Fare() {
        this.value = MIN;
    }

    protected Fare(int value) {
        if (value < 0) {
            throw new InvalidFareException("요금은 음수일 수 없습니다.");
        }
        this.value = value;
    }

    public static Fare from(int value) {
        return new Fare(value);
    }

    public Fare plus(Fare target) {
        return new Fare(value + target.value);
    }

    public Fare minus(Fare target) {
        if (value < target.value) {
            throw new IllegalArgumentException("차감 요금이 더 클 수 없습니다.");
        }
        return new Fare(value - target.value);
    }

    public Fare discountPercent(int discountRate) {
        return new Fare((int) (value * discountRate * 0.01));
    }

    public int getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return value == fare.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public int compareTo(Fare fare) {
        return value < fare.value ? -1 : 1;
    }
}
