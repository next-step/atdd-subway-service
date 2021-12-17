package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeDiscountFarePolicy {
    CHILD(6, 12, AgeDiscountFarePolicy::calculateChildDiscountFare),
    YOUTH(13, 18, AgeDiscountFarePolicy::calculateYouthDiscountFare),
    ADULT(19, Integer.MAX_VALUE, fare -> fare);

    private static final Fare DEDUCTION = Fare.from(350);
    private static final Fare CHILD_DISCOUNT_PERCENT = Fare.from(0.50);
    private static final Fare YOUTH_DISCOUNT_PERCENT = Fare.from(0.20);

    private final int from;
    private final int to;
    private final Function<Fare, Fare> fareFunction;

    AgeDiscountFarePolicy(int from, int to, Function<Fare, Fare> fareFunction) {
        this.from = from;
        this.to = to;
        this.fareFunction = fareFunction;
    }

    public static Fare valueOf(int age, Fare fare) {
        return getAgeDiscountFarePolicy(age).fareFunction.apply(fare);
    }

    private static Fare calculateChildDiscountFare(Fare fare) {
        Fare deductionFare = fare.minus(DEDUCTION);
        return deductionFare.minus(deductionFare.multiply(CHILD_DISCOUNT_PERCENT));
    }

    private static Fare calculateYouthDiscountFare(Fare fare) {
        Fare deductionFare = fare.minus(DEDUCTION);
        return deductionFare.minus(deductionFare.multiply(YOUTH_DISCOUNT_PERCENT));
    }

    private static AgeDiscountFarePolicy getAgeDiscountFarePolicy(int age) {
        return Arrays.stream(values())
                .filter(ageDiscountFarePolicy -> ageDiscountFarePolicy.contain(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean contain(int age) {
        return this.from <= age && this.to >= age;
    }
}
