package nextstep.subway.line.domain;

import static java.lang.Integer.*;
import static java.util.Arrays.*;
import static nextstep.subway.line.domain.Fare.NOT_BENEFIT;

import java.util.function.Predicate;

public enum FareRuleAge {

    INFANT(0,5, NOT_BENEFIT, NOT_BENEFIT),
    CHILD(6, 13, new Fare(350L), new Fare(500L)),  // 0.50
    YOUTH(13, 19, new Fare(350L), new Fare(200L)), // 0.20
    DEFAULT(20, MAX_VALUE, NOT_BENEFIT, NOT_BENEFIT);

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
}
