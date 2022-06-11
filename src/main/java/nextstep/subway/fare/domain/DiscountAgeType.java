package nextstep.subway.fare.domain;

import java.util.Arrays;
import nextstep.subway.policy.ChildrenAgeDiscountPolicy;
import nextstep.subway.policy.DiscountPolicy;
import nextstep.subway.policy.FreeDiscountPolicy;
import nextstep.subway.policy.NonDiscountPolicy;
import nextstep.subway.policy.TeenagerAgeDiscountPolicy;

public enum DiscountAgeType {
    TODDLER(0, 6, new FreeDiscountPolicy()),
    CHILDREN(6, 13, new ChildrenAgeDiscountPolicy()),
    TEENAGER(13, 19, new TeenagerAgeDiscountPolicy()),
    ADULT(19, Integer.MAX_VALUE, new NonDiscountPolicy());

    private static final String INVALID_AGE = "유효하지 않은 나이 정보입니다.";

    private int minAge;
    private int maxAge;
    private DiscountPolicy discountPolicy;

    DiscountAgeType(int minAge, int maxAge, DiscountPolicy discountPolicy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountPolicy = discountPolicy;
    }

    public static DiscountAgeType findDiscountAgeRuleType(int age) {
        return Arrays.stream(values())
                .filter(type -> type.isIncludeAge(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_AGE));
    }

    public DiscountPolicy getDiscountPolicy() {
        return this.discountPolicy;
    }

    private boolean isIncludeAge(int age) {
        return minAge <= age && age < maxAge;
    }
}
