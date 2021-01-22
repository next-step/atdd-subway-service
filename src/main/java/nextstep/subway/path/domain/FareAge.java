package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Predicate;

import static nextstep.subway.path.domain.Fare.DISCOUNT_FARE;

public enum FareAge {
    CHILDREN(value -> value >= 6 && value < 13, 0.5, DISCOUNT_FARE.getValue()),
    YOUTH(value -> value >= 13 && value <= 18, 0.8, DISCOUNT_FARE.getValue()),
    ADULT(value -> value > 18, 1.0, 0);

    private final Predicate<Integer> predicate;
    private final double discountRate;
    private final int discountValue;

    FareAge(Predicate<Integer> predicate, double discountRate, int discountValue) {
        this.predicate = predicate;
        this.discountRate = discountRate;
        this.discountValue = discountValue;
    }

    public static int calculateDiscountedFare(int fare, int age) {
        validateFare(fare);
        return findByAge(age).calculate(fare);
    }

    public double getDiscountRate() {
        return discountRate;
    }

    private static void validateFare(int fare) {
        if (fare < 0) {
            throw new IllegalArgumentException("요금은 0원 이상이 입력되어야합니다.");
        }
    }

    private static FareAge findByAge(int age) {
        return Arrays.stream(FareAge.values())
                .filter(v -> v.predicate.test(age))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("잘못된 나이가 입력되었습니다."));
    }

    private int calculate(int fare) {
        return (int) ((fare - this.discountValue) * this.discountRate);
    }
}
