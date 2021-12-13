package nextstep.subway.line.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare implements Comparable<Fare>{

    @Column
    private int amount;

    protected Fare() {
        this.amount = 0;
    }

    public Fare(int amount) {
        checkValidateFee(amount);
        this.amount = amount;
    }

    public static Fare of(int fee) {
        return new Fare(fee);
    }

    public Fare sum(Fare inputFare) {
        return Fare.of(this.amount + inputFare.amount);
    }

    public Fare minus(Fare inputFare) {
        return Fare.of(this.amount - inputFare.amount);
    }

    public Fare multiply(float discountPercent) {
        return Fare.of((int)(this.amount * discountPercent));
    }

    public int getAmount() {
        return amount;
    }

    private void checkValidateFee(int fee) {
        if (fee < 0) {
            throw new InputDataErrorException(InputDataErrorCode.THE_FEE_IS_LESS_THAN_ZERO);
        }
    }

    @Override
    public String toString() {
        return "Fare{" +
                "amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Fare)) return false;
        Fare fare = (Fare) o;
        return getAmount() == fare.getAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount());
    }

    @Override
    public int compareTo(Fare o) {
        return Integer.compare(this.amount, o.amount);
    }
}
