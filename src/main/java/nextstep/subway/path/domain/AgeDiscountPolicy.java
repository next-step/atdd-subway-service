package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum AgeDiscountPolicy {
    NONE(false, fee -> fee),
    TEEN(true, fee -> {
        return fee.multiply(BigDecimal.valueOf(0.8)).setScale(0, RoundingMode.FLOOR);
    }),
    KID(true, fee -> {
        return fee.multiply(BigDecimal.valueOf(0.5)).setScale(0, RoundingMode.FLOOR);
    });

    private static final Integer MIN_KID = 5;
    private static final Integer MAX_KID = 13;
    private static final Integer MIN_TEEN = 12;
    private static final Integer MAX_TEEN = 19;
    private static final BigDecimal DEFAULT_DISCOUNT_FEE = BigDecimal.valueOf(350);

    private final boolean isDiscountTarget;
    private final AgeDiscount ageDiscount;

    AgeDiscountPolicy(boolean isDiscountTarget, AgeDiscount ageDiscount) {
        this.isDiscountTarget = isDiscountTarget;
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

    public BigDecimal applyDiscount(BigDecimal fee) {
        if (isDiscountTarget) {
            BigDecimal defaultDiscounted = fee.subtract(DEFAULT_DISCOUNT_FEE);
            return this.ageDiscount.apply(defaultDiscounted);
        }
        return fee.setScale(0, RoundingMode.FLOOR);
    }
}
