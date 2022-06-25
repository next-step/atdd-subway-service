package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.fare.policy.DiscountPolicy;
import nextstep.subway.fare.policy.KidsDiscountPolicy;
import nextstep.subway.fare.policy.NoneDiscountPolicy;
import nextstep.subway.fare.policy.TeenagersDiscountPolicy;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public enum AgeType {
    BASIC(19, 65, 0),
    TEENAGERS(13, 19, 0.2f),
    KIDS(6, 13, 0.5f);

    private final int minAge;
    private final int maxAge;
    private final float discountRate;

    AgeType(int minAge, int maxAge, float discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public float getDiscountRate() {
        return this.discountRate;
    }

    public static DiscountPolicy getDiscountPolicy(AuthMember authMember) {
        AgeType ageType = getAgeType(authMember);
        if (TEENAGERS.equals(ageType)) {
            return new TeenagersDiscountPolicy();
        }
        if (KIDS.equals(ageType)) {
            return new KidsDiscountPolicy();
        }
        return new NoneDiscountPolicy();
    }

    private static AgeType getAgeType(AuthMember authMember) {
        if (authMember instanceof LoginMember) {
            LoginMember loginMember = (LoginMember) authMember;
            return Arrays.stream(values())
                    .filter(value -> value.isAgeRange(loginMember.getAge()))
                    .findFirst()
                    .orElse(BASIC);
        }
        return BASIC;
    }

    private boolean isAgeRange(int age) {
        return IntStream.range(minAge, maxAge)
                .boxed()
                .collect(Collectors.toList())
                .contains(age);
    }
}
