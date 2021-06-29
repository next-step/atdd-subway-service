package nextstep.subway.path.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.calculator.OverFare;

@Embeddable
public class Fare {

    @Column(name = "fare")
    private final int amount;

    public Fare() {
        this.amount = 0;
    }

    public Fare(int amount) {
        this.amount = amount;
    }

    public Fare gt(Fare fare) {
        if (this.amount > fare.amount) {
            return this;
        }
        return fare;
    }

    public Fare calculateTotalFare(Distance distance) {
        Fare overFare = new Fare(OverFare.calculate(distance));
        return new Fare(amount + overFare.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return amount == fare.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    public int amount() {
        return amount;
    }
}
