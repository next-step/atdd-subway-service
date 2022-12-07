package nextstep.subway.path.domain;

import java.util.function.Function;

import static nextstep.subway.path.domain.Fare.DISCOUNT_RATE_CHILD;
import static nextstep.subway.path.domain.Fare.DISCOUNT_RATE_TEEN;

enum AgePolicy {
    CHILD(Age::isChild, DISCOUNT_RATE_CHILD),
    TEEN(Age::isTeen, DISCOUNT_RATE_TEEN),
    DEFAULT(null, 0);

    private final Function<Age, Boolean> ageChecker;
    private final double discountRate;

    AgePolicy(Function<Age, Boolean> ageChecker, double discountRate) {
        this.ageChecker = ageChecker;
        this.discountRate = discountRate;
    }

    public Fare applyDiscountFare(final Fare fare) {
        fare.applyDiscountFare(discountRate);
        return fare;
    }

    public boolean isDiscountable(final Age age) {
        if (ageChecker == null) return false;
        return ageChecker.apply(age);
    }
}
