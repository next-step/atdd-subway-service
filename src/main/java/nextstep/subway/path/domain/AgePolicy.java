package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.MemberType;
import nextstep.subway.member.domain.Age;
import nextstep.subway.member.domain.AgeType;

public class AgePolicy {

    private static final int FIX_DISCOUNT_AMOUNT = 350;
    private final Age age;
    private final MemberType memberType;

    public static AgePolicy from(Age age, MemberType memberType) {
        return new AgePolicy(age, memberType);
    }

    private AgePolicy(Age age, MemberType memberType) {
        this.age = age;
        this.memberType = memberType;
    }


    public int discount(int fee) {
        if (memberType.isNotLogin()) {
            return fee;
        }

        return applyPolicy(fee);
    }

    private int applyPolicy(int fee) {
        AgeType ageType = age.getType();

        if (ageType.isDiscountTarget()) {
            return (int) ((fee - FIX_DISCOUNT_AMOUNT) * ageType.getDiscountPercent());
        }

        return fee;
    }
}
