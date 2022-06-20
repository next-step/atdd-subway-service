package nextstep.subway.path.domain;

import java.util.Objects;

public class FeeV2 {
    private static final int DEFAULT_FEE = 1250;

    private int fee;

    public FeeV2() {
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
        FeeV2 feeV2 = (FeeV2) o;
        return fee == feeV2.fee;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fee);
    }
}
