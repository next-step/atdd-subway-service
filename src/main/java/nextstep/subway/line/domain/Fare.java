package nextstep.subway.line.domain;

import nextstep.subway.exception.BadRequestException;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class Fare {

    private final BigDecimal amount;

    protected Fare() {
        this.amount = BigDecimal.ZERO;
    }

    private Fare(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("요금은 1 이상의 값으로 입력해주세요.");
        }
        this.amount = amount;
    }

    public static Fare from(int amount) {
        return new Fare(BigDecimal.valueOf(amount));
    }

    public static Fare from(BigDecimal amount) {
        return new Fare(amount);
    }

    public Fare plus(Fare amount) {
        return new Fare(this.amount.add(amount.amount));
    }

    public Fare minus(Fare amount) {
        return new Fare(this.amount.subtract(amount.amount));
    }

    public BigDecimal value() {
        return amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fare fare = (Fare) o;
        return Objects.equals(amount, fare.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
