package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public enum AgeDiscountPolicy {
    NONE(false, BigDecimal.ZERO),
    TEEN(true, BigDecimal.valueOf(0.2)),
    KID(true, BigDecimal.valueOf(0.5));

    private static final Integer MIN_KID = 5;
    private static final Integer MAX_KID = 13;
    private static final Integer MIN_TEEN = 12;
    private static final Integer MAX_TEEN = 19;
    private static final BigDecimal DEFAULT_DISCOUNT_FEE = BigDecimal.valueOf(350);

    private final boolean isDiscountTarget;
    private final BigDecimal discountRatio;

    AgeDiscountPolicy(boolean isDiscountTarget, BigDecimal discountRatio) {
        this.isDiscountTarget = isDiscountTarget;
        this.discountRatio = discountRatio;
    }

    public static AgeDiscountPolicy find(Integer age) {
        if (AgeDiscountPolicy.isKid(age)) {
            return AgeDiscountPolicy.KID;
        }
        if (AgeDiscountPolicy.isTeen(age)) {
            return AgeDiscountPolicy.TEEN;
        }
        return AgeDiscountPolicy.NONE;
    }

    public BigDecimal applyDiscount(BigDecimal fee) {
        if (isDiscountTarget) {
            return this.roundingFloor(this.calculateRemainAfterDiscounting(fee));
        }
        return this.roundingFloor(fee);
    }

    private static boolean isKid(Integer age) {
        return age > MIN_KID && age < MAX_KID;
    }

    private static boolean isTeen(Integer age) {
        return age > MIN_TEEN && age < MAX_TEEN;
    }

    private BigDecimal roundingFloor(BigDecimal value) {
        return value.setScale(0, RoundingMode.FLOOR);
    }

    private BigDecimal calculateRemainAfterDiscounting(BigDecimal fee) {
        BigDecimal defaultDiscounted = fee.subtract(DEFAULT_DISCOUNT_FEE);
        return this.roundingFloor(defaultDiscounted.multiply(this.calculateRemainRatio()));
    }

    private BigDecimal calculateRemainRatio() {
        return BigDecimal.ONE.subtract(this.discountRatio);
    }
}
