package nextstep.subway.path.domain;

import static nextstep.subway.path.domain.FarePolicy.ADULT;

import java.util.Objects;

public class Fare {

    private final int amount;

    public Fare() {
        amount = ADULT.getDefaultAmount();
    }

    public Fare(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public boolean isFree() {
        return amount == 0;
    }

    public Fare plus(int amount) {
        return new Fare(this.amount + amount);
    }

    public Fare minus(int amount) {
        return new Fare(this.amount - amount);
    }

    public Fare applyDiscountRate(int discountRate) {
        double actually = amount - ((discountRate * 0.01) * amount);
        return new Fare((int) actually);
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
