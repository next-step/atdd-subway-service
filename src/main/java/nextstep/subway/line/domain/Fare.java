package nextstep.subway.line.domain;

import nextstep.subway.exception.InputDataErrorCode;
import nextstep.subway.exception.InputDataErrorException;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fare implements Comparable<Fare> {
    private static final int ZERO = 0;

    @Column
    private final int amount;

    protected Fare() {
        this.amount = ZERO;
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
        return Fare.of((int) (this.amount * discountPercent));
    }

    public int getAmount() {
        return amount;
    }

    public static Fare createZeroFare(){
        return new Fare(ZERO);
    }

    private void checkValidateFee(int fee) {
        if (fee < ZERO) {
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
        return Integer.compare(this.amount, o.getAmount());
    }
}
