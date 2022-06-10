package nextstep.subway.fare.domain;

import java.util.Arrays;

public enum DiscountAgeRuleType {
    TODDLER(0, 6),
    CHILDREN(6, 13),
    TEENAGER(13, 19),
    ADULT(19, Integer.MAX_VALUE);

    private static final String INVALID_AGE = "유효하지 않은 나이 정보입니다.";
    private int minAge;
    private int maxAge;

    DiscountAgeRuleType(int minAge, int maxAge) {
        this.minAge = minAge;
        this.maxAge = maxAge;
    }

    public static DiscountAgeRuleType findDiscountAgeRuleType(int age) {
        return Arrays.asList(values()).stream()
                .filter(type -> type.isIncludeAge(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(INVALID_AGE));
    }

    private boolean isIncludeAge(int age) {
        return minAge <= age && age < maxAge;
    }
}
