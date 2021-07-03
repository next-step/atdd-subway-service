package nextstep.subway.line.domain;

import lombok.EqualsAndHashCode;

import javax.persistence.Embeddable;

@Embeddable
@EqualsAndHashCode(of = "charges")
public class Charges {
    private static final int MINIMUM_PRICE = 0;

    private final int charges;

    public Charges() {
        this(MINIMUM_PRICE);
    }

    public Charges(final int charges) {
        if (charges < MINIMUM_PRICE) {
            throw new IllegalArgumentException("잘못된 요금입니다.");
        }

        this.charges = charges;
    }

    @Override
    public String toString() {
        return String.valueOf(charges);
    }
}
