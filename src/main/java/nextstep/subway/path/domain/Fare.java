package nextstep.subway.path.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Fare {

    public static final String FARE_CANNOT_BE_NEGATIVE = "요금은 음수일 수 없습니다.";

    @Column(name = "fare")
    private final int amount;

    protected Fare() {
        amount = 0;
    }

    private Fare(int amount) {
        validationNegativeNumber(amount);
        this.amount = amount;
    }

    private void validationNegativeNumber(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException(FARE_CANNOT_BE_NEGATIVE);
        }
    }

    public static Fare wonOf(int amount) {
        return new Fare(amount);
    }

    public int getAmount() {
        return amount;
    }

    public boolean isFree() {
        return amount == 0;
    }

    public Fare plus(int amount) {
        return plus(Fare.wonOf(amount));
    }

    public Fare plus(Fare fare) {
        return Fare.wonOf(this.amount + fare.amount);
    }

    public Fare minus(int amount) {
        return minus(Fare.wonOf(amount));
    }

    public Fare minus(Fare fare) {
        return Fare.wonOf(this.amount - fare.amount);
    }

    public Fare applyDiscountRate(int discountRate) {
        double actually = amount - ((discountRate * 0.01) * amount);
        return Fare.wonOf((int) actually);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fare fare = (Fare) o;
        return amount == fare.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return String.valueOf(amount);
    }
}
