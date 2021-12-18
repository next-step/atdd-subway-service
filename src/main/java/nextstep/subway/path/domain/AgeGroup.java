package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeGroup {
    BABY(0, 5, 0, 100),
    CHILDREN(6, 12, 350, 50),
    TEENAGER(13, 18, 350, 20),
    ADULT(19, Integer.MAX_VALUE, 0, 0);

    public static final String NEGATIVE_AGE_ERR_MSG = "나이는 음수가 될 수 없습니다.";
    private final int minAge;
    private final int maxAge;
    private final int discount;
    private final int discountPercent;

    AgeGroup(final int minAge, final int maxAge, final int discount, final int discountPercent) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discount = discount;
        this.discountPercent = discountPercent;
    }

    static AgeGroup of(int age) {
        return Arrays.stream(values())
            .filter(ageGroup -> ageGroup.contains(age))
            .findAny()
            .orElseThrow(() -> new IllegalArgumentException(NEGATIVE_AGE_ERR_MSG));
    }

    private boolean contains(int age) {
        return age >= minAge && age <= maxAge;
    }

    int applyDiscount(int fare) {
        return (fare - discount) * (100 - discountPercent) / 100;
    }
}
