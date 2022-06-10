package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

public enum DiscountAgeRule {
    TODDLER(0, 6, fare -> 0),
    CHILDREN(6, 13, fare -> discountByPercent(fare, 50)),
    TEENAGER(13, 19, fare -> discountByPercent(fare, 80)),
    ADULT(19, Integer.MAX_VALUE, fare -> fare);

    private static int discountByPercent(int fare, int percent) {
        return (int) ((fare - BASIC_DISCOUNT_FARE) * (percent * 0.01));
    }

    private static final int BASIC_DISCOUNT_FARE = 350;
    private static final String INVALID_AGE = "유효하지 않은 나이 정보입니다.";

    private int minAge;
    private int maxAge;
    private IntUnaryOperator discountCalculate;

    DiscountAgeRule(int minAge, int maxAge, IntUnaryOperator discountCalculate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountCalculate = discountCalculate;
    }

    public static DiscountAgeRule findDiscountAgeRuleType(int age) {
        return Arrays.stream(values())
                .filter(type -> type.isIncludeAge(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_AGE));
    }

    public static int discountFare(int fare, int age) {
        DiscountAgeRule type = findDiscountAgeRuleType(age);
        return type.discountCalculate.applyAsInt(fare);
    }

    private boolean isIncludeAge(int age) {
        return minAge <= age && age < maxAge;
    }
}
