package nextstep.subway.path.domain;

import java.util.Objects;

public class Fee {
    private static final int DEFAULT_FEE = 1250;

    private int fee;

    public Fee() {
        this.fee = DEFAULT_FEE;
    }

    public int getFee() {
        return fee;
    }

    public void add(int extraFee) {
        this.fee += extraFee;
    }

    public void update(int fee) {
        this.fee = fee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Fee fee = (Fee) o;
        return this.fee == fee.fee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fee);
    }
}
