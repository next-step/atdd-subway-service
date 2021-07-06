package nextstep.subway.line.domain;

import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode(of = "fee")
public class Fee implements Comparable<Fee> {
    private static final int MINIMUM_PRICE = 0;

    private final int fee;

    public Fee() {
        this(MINIMUM_PRICE);
    }

    public Fee(final int fee) {
        if (fee < MINIMUM_PRICE) {
            throw new IllegalArgumentException("잘못된 요금입니다.");
        }

        this.fee = fee;
    }

    public int getFee() {
        return fee;
    }

    @Override
    public int compareTo(final Fee fee) {
        return Integer.compare(fee.fee, this.fee);
    }

    @Override
    public String toString() {
        return String.valueOf(fee);
    }
}
