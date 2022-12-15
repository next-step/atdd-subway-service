package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.MemberType;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeType;

public class DefaultAgePolicy implements AgeStrategy {

    private final Age age;
    private final MemberType memberType;
    private final int fee;

    private static final int FIX_DISCOUNT_AMOUNT = 350;

    public DefaultAgePolicy(Age age, MemberType memberType, int fee) {
        this.age = age;
        this.memberType = memberType;
        this.fee = fee;
    }

    @Override
    public int discount() {
        if (memberType.isNotLogin()) {
            return fee;
        }

        if (age.getType().isDiscountTarget()) {
            int deductFee = fee - FIX_DISCOUNT_AMOUNT;
            return (int) (deductFee - deductFee * age.getType().getDiscountPercent());
        }

        return fee;
    }
}
