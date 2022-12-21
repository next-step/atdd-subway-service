package nextstep.subway.fare.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {
    @Column(nullable = false)
    private int fare;

    protected Fare() {}

    private Fare(int fare) {
        validateRange(fare);
        this.fare = fare;
    }

    public static Fare from(int fare) {
        return new Fare(fare);
    }

    private void validateRange(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 음수가 될 수 없습니다.");
        }
    }

    public Fare plus(Fare addFare) {
        return new Fare(this.fare + addFare.value());
    }

    public int value() {
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
}
