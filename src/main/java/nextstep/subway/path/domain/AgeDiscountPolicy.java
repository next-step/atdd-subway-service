package nextstep.subway.path.domain;

import nextstep.subway.member.domain.Age;

public class AgeDiscountPolicy {
    private static final int DEDUCTIBLE_AMOUNT = 350;
    private static final double YOUTH_DISCOUNT_PERCENT = 0.8;
    private static final double CHILD_DISCOUNT_PERCENT = 0.5;


    private AgeDiscountPolicy() {
    }

    public static Fare discount(Fare fare, Age age) {
        if (age.isYouth()) {
            return fare.minus(DEDUCTIBLE_AMOUNT).apply(YOUTH_DISCOUNT_PERCENT);
        }
        if (age.isChild()) {
            return fare.minus(DEDUCTIBLE_AMOUNT).apply(CHILD_DISCOUNT_PERCENT);
        }

        return fare;
    }
}
