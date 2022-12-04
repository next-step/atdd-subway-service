package nextstep.subway.path.domain;

import nextstep.subway.auth.domain.AuthMember;
import nextstep.subway.path.policy.AgeDiscountPolicy;
import nextstep.subway.path.policy.KidsAgeDiscountPolicy;
import nextstep.subway.path.policy.BasicAgeDiscountPolicy;
import nextstep.subway.path.policy.TeenagersAgeDiscountPolicy;

import java.util.Arrays;

public enum AgeFare {

    KIDS(6, 13, new KidsAgeDiscountPolicy()),
    TEENAGERS(13, 19, new TeenagersAgeDiscountPolicy()),
    BASIC(19, 65, new BasicAgeDiscountPolicy()),
    ;

    private final int minAge;
    private final int maxAge;
    private final AgeDiscountPolicy ageDiscountPolicy;

    AgeFare(int minAge, int maxAge, AgeDiscountPolicy ageDiscountPolicy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.ageDiscountPolicy = ageDiscountPolicy;
    }

    public static AgeDiscountPolicy getDiscountPolicy(AuthMember authMember) {
        return getAgeType(authMember).ageDiscountPolicy;
    }

    private static AgeFare getAgeType(AuthMember authMember) {
        return Arrays.stream(values())
                .filter(value -> value.isAgeRange(authMember.getAge()))
                .findFirst()
                .orElse(BASIC);
    }

    private boolean isAgeRange(int age) {
        return minAge <= age && age < maxAge;
    }

}
