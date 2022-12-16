package nextstep.subway.line.domain;

import nextstep.subway.enums.ErrorMessage;

import java.math.BigDecimal;
import java.util.Arrays;

public enum DiscountPolicyByAge {
    BETWEEN_NINETEEN_HUNDRED(19, 100, 0),
    BETWEEN_THIRTEEN_AND_EIGHTEEN(13, 18, 0.2),
    BETWEEN_SIX_AND_TWELVE(6, 12, 0.5),
    BETWEEN_ONE_AND_FIVE(1, 5, 1),
    ;

    private static final BigDecimal DEFAULT_DISCOUNT_FARE = BigDecimal.valueOf(350);

    private final int minAge;
    private final int maxAge;
    private final double discountRate;

    DiscountPolicyByAge(int minAge, int maxAge, double discountRate) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
    }

    public static Fare calculate(Fare fare, int age) {
        return Arrays.stream(values())
                .filter(policy -> policy.checkAgeRange(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.INVALID_AGE_RANGE.getMessage()))
                .discount(fare);
    }

    private boolean checkAgeRange(int age) {
        return this.minAge <= age && this.maxAge >= age;
    }

    private Fare discount(Fare fare) {
        if (this.discountRate == 0) {
            return fare;
        }
        return Fare.from(
                fare.value().subtract(fare.value()
                        .subtract(DEFAULT_DISCOUNT_FARE)
                        .multiply(BigDecimal.valueOf(discountRate))).intValue()
        );
    }
}
