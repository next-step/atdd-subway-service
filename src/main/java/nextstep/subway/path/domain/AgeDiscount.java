package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeDiscount {
    TODDLER(1, 5, fare -> fare),
    CHILDREN(6, 12, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGER(13, 18, fare -> (int) ((fare - 350) * 0.8)),
    ADULT(18, Integer.MAX_VALUE, fare -> fare);

    public static final String NEGATIVE_AGE_EXCEPTION_MESSAGE = "나이가 한살 미만일 수 없습니다.";

    private final int minAge;
    private final int maxAge;
    private final DistanceFareStrategy distanceFareStrategy;

    AgeDiscount(int minAge, int maxAge, DistanceFareStrategy distanceFareStrategy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.distanceFareStrategy = distanceFareStrategy;
    }

    public int discountFare(int fare) {
        return distanceFareStrategy.calculateDistanceFare(fare);
    }

    public static AgeDiscount findAgeDiscountByAge(int age){
        return Arrays.stream(AgeDiscount.values())
                .filter(ageDiscount -> ageDiscount.isWithinRange(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(NEGATIVE_AGE_EXCEPTION_MESSAGE));
    }

    private boolean isWithinRange(int age) {
        return minAge <= age && age <= maxAge;
    }
}
