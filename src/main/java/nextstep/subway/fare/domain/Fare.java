package nextstep.subway.fare.domain;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class Fare implements Comparable<Fare> {

    @Column(name = "fare", nullable = false)
    private int fare;

    private Fare(int fare) {
        validate(fare);
        this.fare = fare;
    }

    public static Fare of(int fare) {
        return new Fare(fare);
    }

    private void validate(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 0원 이하일 수 없습니다.");
        }
    }

    public Fare plus(Fare otherFare) {
        return new Fare(this.fare + otherFare.fare);
    }

    public Fare minus(Fare fare) {
        return new Fare(this.fare - fare.fare);
    }

    public Fare multiply(double multiplier) {
        return new Fare((int) (this.fare * multiplier));
    }

    public int getFare() {
        return fare;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(fare);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare1 = (Fare) o;
        return fare == fare1.fare;
    }

    @Override
    public int compareTo(Fare otherFare) {
        return Integer.compare(this.fare, otherFare.fare);
    }
}
