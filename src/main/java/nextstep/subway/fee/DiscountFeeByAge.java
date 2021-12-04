package nextstep.subway.fee;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum DiscountFeeByAge {

    ADULT(DiscountFeeByAge::isAdult, DiscountFeeByAge::calculateAdult),
    TEENAGER(DiscountFeeByAge::isTeenager, DiscountFeeByAge::calculateTeenager),
    CHILD(DiscountFeeByAge::isChild, DiscountFeeByAge::calculateChild);

    private final Predicate<Integer> checkAge;
    private final Function<Long, Integer> expression;

    private static final int DEDUCTIBLE_FEE = 350;
    private static final int TEENAGER_AGE_LOWER_LIMIT = 13;
    private static final int TEENAGER_AGE_UPPER_LIMIT = 19;
    private static final int CHILD_AGE_LOWER_LIMIT = 6;
    private static final int CHILD_AGE_UPPER_LIMIT = 12;
    private static final int ADULT_AGE_LOWER_LIMIT = 20;

    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILD_DISCOUNT_RATE = 0.5;

    DiscountFeeByAge(Predicate<Integer> checkAge, Function<Long, Integer> expression) {
        this.checkAge = checkAge;
        this.expression = expression;
    }

    public static Integer getFee(int age, long fee) {

        return Arrays.stream(values())
                .filter(value -> value.checkAge.test(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("age가 잘못되었습니다."))
                .expression.apply(fee);
    }

    private static boolean isAdult(int age) {
        return age >= ADULT_AGE_LOWER_LIMIT;
    }

    private static boolean isChild(int age) {
        return age >= CHILD_AGE_LOWER_LIMIT && age < CHILD_AGE_UPPER_LIMIT;
    }

    private static boolean isTeenager(int age) {
        return age >= TEENAGER_AGE_LOWER_LIMIT && age < TEENAGER_AGE_UPPER_LIMIT;
    }

    private static int calculateAdult(Long fee) {
        return Math.toIntExact(fee);
    }

    private static int calculateTeenager(Long fee) {
        return (int) ((fee - DEDUCTIBLE_FEE) * (1 - TEENAGER_DISCOUNT_RATE));
    }

    private static int calculateChild(Long fee) {
        return (int) ((fee - DEDUCTIBLE_FEE) * (1 - CHILD_DISCOUNT_RATE));
    }
}
