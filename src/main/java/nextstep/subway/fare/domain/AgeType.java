package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.fare.policy.DiscountPolicy;
import nextstep.subway.fare.policy.KidsDiscountPolicy;
import nextstep.subway.fare.policy.NoneDiscountPolicy;
import nextstep.subway.fare.policy.TeenagersDiscountPolicy;

import java.util.Arrays;

public enum AgeType {
    BASIC(19, 65, new NoneDiscountPolicy()),
    TEENAGERS(13, 19, new TeenagersDiscountPolicy()),
    KIDS(6, 13, new KidsDiscountPolicy());

    private final int minAge;
    private final int maxAge;
    private final DiscountPolicy discountPolicy;

    AgeType(int minAge, int maxAge, DiscountPolicy discountPolicy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountPolicy = discountPolicy;
    }

    public static DiscountPolicy getDiscountPolicy(AuthMember authMember) {
        return getAgeType(authMember).discountPolicy;
    }

    private static AgeType getAgeType(AuthMember authMember) {
        return Arrays.stream(values())
                .filter(value -> value.isAgeRange(authMember.getAge()))
                .findFirst()
                .orElse(BASIC);
    }

    private boolean isAgeRange(int age) {
        return minAge <= age && age < maxAge;
    }
}