package nextstep.subway.auth.domain;

import nextstep.subway.member.domain.Age;

import java.util.Arrays;
import java.util.function.Predicate;

public enum DiscountType {
    CHILD(6, 12, new ChildDiscountPolicy()),
    YOUTH(13, 18, new YouthDiscountPolicy()),
    ADULT(19, Integer.MAX_VALUE, new AdultDiscountPolicy());

    private int start;
    private int end;
    private DiscountPolicy discountPolicy;

    DiscountType(int start, int end, DiscountPolicy discountPolicy) {
        this.start = start;
        this.end = end;
        this.discountPolicy = discountPolicy;
    }

    public static DiscountPolicy findDiscountPolicy(Age age) {
        DiscountType discountType = Arrays.stream(values())
                .filter(isAgeInRange(age.getValue()))
                .findFirst()
                .orElse(ADULT);
        return discountType.getDiscountPolicy();
    }

    private static Predicate<DiscountType> isAgeInRange(int age) {
        return type -> type.start <= age && age <= type.end;
    }

    public DiscountPolicy getDiscountPolicy() {
        return discountPolicy;
    }
}
