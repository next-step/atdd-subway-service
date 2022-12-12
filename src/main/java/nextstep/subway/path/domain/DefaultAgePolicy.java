package nextstep.subway.path.domain;

import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeType;

public class DefaultAgePolicy implements AgeStrategy {

    private static final int FIX_DISCOUNT_AMOUNT = 350;

    @Override
    public int discount(int fee, Age age) {
        AgeType ageType = age.getType();

        if (ageType.isDiscountTarget()) {
            int deductFee = fee - FIX_DISCOUNT_AMOUNT;
            return (int) (deductFee - deductFee * ageType.getDiscountPercent());
        }

        return fee;
    }
}
