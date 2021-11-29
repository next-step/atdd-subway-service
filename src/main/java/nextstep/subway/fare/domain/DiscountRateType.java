package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.IntUnaryOperator;

import nextstep.subway.utils.StreamUtils;

public enum DiscountRateType {
    TODDLER(1, 6, fare -> 0),
    CHILD(6, 13, fare -> (int) ((fare - 350) * 0.5)),
    TEENAGER(13, 19, fare -> (int) ((fare - 350) * 0.8)),
    ADULT(19, Integer.MAX_VALUE, fare -> fare);

    private static final String INVALID_AGE_ERROR_MESSAGE = "나이 정보가 유효하지 않습니다.";
    private final int minAge;
    private final int maxAge;
    private final IntUnaryOperator discountCalculator;

    DiscountRateType(int minAge, int maxAge, IntUnaryOperator discountCalculator) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountCalculator = discountCalculator;
    }

    public static int discountBy(int fare, int age) {
        DiscountRateType discountRateType = StreamUtils.filterAndFindFirst(Arrays.asList(values()),
                                                                           type -> type.isIncluded(age))
                                                       .orElseThrow(() -> new IllegalArgumentException(INVALID_AGE_ERROR_MESSAGE));

        return discountRateType.discountCalculator.applyAsInt(fare);
    }

    private boolean isIncluded(Integer age) {
        return minAge <= age && age < maxAge;
    }
}
