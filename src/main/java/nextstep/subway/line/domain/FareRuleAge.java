package nextstep.subway.line.domain;

import static java.lang.Integer.MAX_VALUE;
import static java.util.Arrays.stream;
import static nextstep.subway.line.domain.Fare.NOT_BENEFIT;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.DEDUCTION_350;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.DISCOUNT_200;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.DISCOUNT_500;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.MAX_AGE_FIVE;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.MAX_AGE_NINETEEN;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.MAX_AGE_THIRTEEN;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.MIN_AGE_SIX;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.MIN_AGE_THIRTEEN;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.MIN_AGE_TWENTY;
import static nextstep.subway.line.domain.FareRuleAge.FareRuleAgeConstant.MIN_AGE_ZERO;

import java.util.function.Predicate;

public enum FareRuleAge {

    INFANT(MIN_AGE_ZERO, MAX_AGE_FIVE, NOT_BENEFIT, NOT_BENEFIT),
    CHILD(MIN_AGE_SIX, MAX_AGE_THIRTEEN, DEDUCTION_350, DISCOUNT_500),  // 0.50
    YOUTH(MIN_AGE_THIRTEEN, MAX_AGE_NINETEEN, DEDUCTION_350, DISCOUNT_200), // 0.20
    DEFAULT(MIN_AGE_TWENTY, MAX_VALUE, NOT_BENEFIT, NOT_BENEFIT);

    private int minAge;
    private int maxAge;
    private Fare deduction;
    private Fare discountAmount;

    FareRuleAge(int minAge, int maxAge, Fare deduction, Fare discountAmount) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.deduction = deduction;
        this.discountAmount = discountAmount;
    }

    public static Fare calculate(Integer age) {
        FareRuleAge findRule = findRuleByAge(age);
        return Fare.defaultFare
            .subtract(findRule.deduction)
            .subtract(findRule.discountAmount);
    }

    private static FareRuleAge findRuleByAge(Integer age) {
        return stream(FareRuleAge.values())
            .filter(isAgeBetween(age))
            .findFirst()
            .orElse(DEFAULT);
    }

    private static Predicate<FareRuleAge> isAgeBetween(Integer age) {
        return s -> age >= s.minAge && age < s.maxAge;
    }

    protected static class FareRuleAgeConstant {
        public static final int MIN_AGE_ZERO = 0;
        public static final int MAX_AGE_FIVE = 5;
        public static final int MIN_AGE_SIX = 6;
        public static final int MAX_AGE_THIRTEEN = 13;
        public static final int MIN_AGE_THIRTEEN = 13;
        public static final int MAX_AGE_NINETEEN = 19;
        public static final int MIN_AGE_TWENTY = 20;
        public static final Fare DEDUCTION_350 = new Fare(350L);
        public static final Fare DISCOUNT_500 = new Fare(500L);
        public static final Fare DISCOUNT_200 = new Fare(200L);
    }
}
