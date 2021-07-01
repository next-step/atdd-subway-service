package nextstep.subway.path.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import nextstep.subway.error.CustomException;
import nextstep.subway.error.ErrorMessage;
import nextstep.subway.line.domain.Distance;
import nextstep.subway.path.calculator.OverFareByDistance;
import nextstep.subway.path.memberfarepolicy.MemberDiscountPolicy;

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

    public Fare gt(Fare fare) {
        if (this.amount > fare.amount) {
            return this;
        }
        return fare;
    }

    public Fare calculateTotalFare(Distance distance, MemberDiscountPolicy policy) {
        Fare totalFare = new Fare(amount + OverFareByDistance.calculate(distance));
        return policy.applyDiscount(totalFare);
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
