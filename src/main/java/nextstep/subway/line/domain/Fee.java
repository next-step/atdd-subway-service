package nextstep.subway.line.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class Fee {
    public static final int DEFAULT_FEE_AMOUNT = 900;

    @Column(name = "fee")
    private final int amount;

    public Fee() {
        this.amount = DEFAULT_FEE_AMOUNT;
    }

    public Fee(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fee fee = (Fee) o;
        return amount == fee.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
