package nextstep.subway.path.domain;

import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare implements Comparable<Fare> {
    private static final int MIN = 0;
    private static final String ERROR_MESSAGE_NEGATIVE_NUMBER = "요금은 음수일 수 없습니다.";
    private static final String ERROR_MESSAGE_INVALID_MINUS = "차감 요금이 더 클 수 없습니다.";

    private final int fare;

    public Fare() {
        this.fare = MIN;
    }

    protected Fare(int fare) {
        validate(fare);
        this.fare = fare;
    }

    public static Fare of(int fare) {
        return new Fare(fare);
    }

    private void validate(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NEGATIVE_NUMBER);
        }
    }

    public Fare plus(Fare other) {
        return new Fare(fare + other.fare);
    }

    public Fare minus(Fare other) {
        if (fare < other.fare) {
            throw new IllegalArgumentException(ERROR_MESSAGE_INVALID_MINUS);
        }
        return new Fare(fare - other.fare);
    }

    public Fare discountByPercent(double discountRate) {
        return new Fare((int) (fare * (1 - discountRate)));
    }

    public int getFare() {
        return fare;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare that = (Fare) o;
        return Objects.equals(fare, that.fare);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fare);
    }

    @Override
    public int compareTo(Fare other) {
        return fare < other.fare ? -1 : 1;
    }
}
