package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum AgeDiscountPolicy {
    NONE(extraFee -> extraFee.setScale(0, RoundingMode.FLOOR)),
    TEEN(extraFee -> {
        BigDecimal discountConstantValue = extraFee.subtract(BigDecimal.valueOf(350));
        return discountConstantValue.multiply(BigDecimal.valueOf(0.8)).setScale(0, RoundingMode.FLOOR);
    }),
    KID(extraFee -> {
        BigDecimal discountConstantValue = extraFee.subtract(BigDecimal.valueOf(350));
        return discountConstantValue.multiply(BigDecimal.valueOf(0.5)).setScale(0, RoundingMode.FLOOR);
    });

    private static final Integer MIN_KID = 5;
    private static final Integer MAX_KID = 13;
    private static final Integer MIN_TEEN = 12;
    private static final Integer MAX_TEEN = 19;

    private final AgeDiscount ageDiscount;

    AgeDiscountPolicy(final AgeDiscount ageDiscount) {
        this.ageDiscount = ageDiscount;
    }

    public static AgeDiscountPolicy find(Integer age) {
        if (age > MIN_KID && age < MAX_KID) {
            return AgeDiscountPolicy.KID;
        }
        if (age > MIN_TEEN && age < MAX_TEEN) {
            return AgeDiscountPolicy.TEEN;
        }
        return AgeDiscountPolicy.NONE;
    }

    public BigDecimal applyDiscount(BigDecimal extraFee) {
        return this.ageDiscount.apply(extraFee);
    }
}
