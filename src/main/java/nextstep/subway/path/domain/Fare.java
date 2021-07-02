package nextstep.subway.path.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nextstep.subway.error.CustomException;
import nextstep.subway.error.ErrorMessage;

@Embeddable
public class Fare {

    @Column(name = "fare")
    private final int amount;

    public Fare() {
        this.amount = 0;
    }

    public Fare(int amount) {
        if (amount < 0) {
            throw new CustomException(ErrorMessage.INVALID_FARE_AMOUNT);
        }
        this.amount = amount;
    }

    public Fare sum(Fare fare) {
        return new Fare(amount + fare.amount);
    }

    public Fare sub(Fare fare) {
        return new Fare(amount - fare.amount);
    }

    public Fare discountFare(double discountPer) {
        return new Fare((int) Math.ceil(amount * discountPer));
    }

    public Fare gt(Fare fare) {
        if (this.amount > fare.amount) {
            return this;
        }
        return fare;
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
