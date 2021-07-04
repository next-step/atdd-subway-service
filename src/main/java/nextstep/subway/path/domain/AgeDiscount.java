package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeDiscount {
    TODDLER(1, 5, fare -> fare),
    CHILDREN(6, 12, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGER(13, 18, fare -> (int) ((fare - 350) * 0.8)),
    ADULT(18, Integer.MAX_VALUE, fare -> fare);

    private final int minAge;
    private final int maxAge;
    private final AgeDiscountStrategy ageDiscountStrategy;

    AgeDiscount(int minAge, int maxAge, AgeDiscountStrategy ageDiscountStrategy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.ageDiscountStrategy = ageDiscountStrategy;
    }

    public int discountFare(int fare) {
        return ageDiscountStrategy.discount(fare);
    }

    public static AgeDiscount findAgeDiscountByAge(int age){
        return Arrays.stream(AgeDiscount.values())
                .filter(ageDiscount -> ageDiscount.isWithinRange(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("나이: %2d - 할인 범위를 벗어났습니다.", age)));
    }

    private boolean isWithinRange(int age) {
        return minAge <= age && age <= maxAge;
    }
}
