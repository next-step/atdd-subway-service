package nextstep.subway.fare.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare implements Comparable<Fare> {
    private static final int MIN_NUM = 0;
    @Column
    private int fare;

    private Fare(int fare) {
        validateFare(fare);
        this.fare = fare;
    }

    protected Fare() {
    }

    public static Fare valueOf(int fare) {
        return new Fare(fare);
    }

    private void validateFare(int fare) {
        if (fare < MIN_NUM) {
            throw new IllegalArgumentException("음수는 유효하지 않습니다.");
        }
    }

    public void add(Fare money) {
        this.fare = Math.addExact(this.fare, money.fare);
    }

    public void minus(Fare fare) {
        this.fare = Math.subtractExact(this.fare, fare.fare());
        validateFare(this.fare);
    }

    public void multiply(int percentage) {
        this.fare = Math.multiplyExact(this.fare, percentage);
    }

    public void divideBy(int number) {
        if (isZero(number)) {
            throw new IllegalArgumentException("0으로 나눌 수 없습니다.");
        }
        this.fare = Math.floorDiv(this.fare, number);
    }

    private boolean isZero(int number) {
        return number == MIN_NUM;
    }

    public int fare() {
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
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public int compareTo(Fare other) {
        return Integer.compare(fare, other.fare);
    }
}
