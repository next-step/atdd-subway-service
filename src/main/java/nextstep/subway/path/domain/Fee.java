package nextstep.subway.path.domain;

import java.math.BigDecimal;

public class Fee {
    private final BigDecimal originalFee;
    private final AgeDiscountPolicy ageDiscountPolicy;

    public Fee(final BigDecimal originalFee, final AgeDiscountPolicy ageDiscountPolicy) {
        this.originalFee = originalFee;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public BigDecimal calculate() {
        return ageDiscountPolicy.applyDiscount(originalFee);
    }
}
