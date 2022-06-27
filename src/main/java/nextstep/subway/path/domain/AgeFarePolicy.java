package nextstep.subway.path.domain;

import org.springframework.stereotype.Component;

@Component
public class AgeFarePolicy {
    private static final Fare DEDUCT_FARE = Fare.of(350);
    private static final int CHILDREN_MIN_AGE = 6;
    private static final int CHILDREN_BOUNDARY_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 19;

    public Fare discount(Fare fare, int age) {
        if (isChildren(age)) {
            Fare discountFare = fare.minus(DEDUCT_FARE).discountPercent(50);
            return fare.minus(discountFare);
        }
        if (isTeenager(age)) {
            Fare discountFare = fare.minus(DEDUCT_FARE).discountPercent(20);
            return fare.minus(discountFare);
        }
        return fare;
    }

    private boolean isChildren(int age) {
        return age >= CHILDREN_MIN_AGE && age < CHILDREN_BOUNDARY_AGE;
    }

    private boolean isTeenager(int age) {
        return age >= CHILDREN_BOUNDARY_AGE && age < TEENAGER_MAX_AGE;
    }
}
