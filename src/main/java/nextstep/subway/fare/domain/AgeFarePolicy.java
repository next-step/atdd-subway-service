package nextstep.subway.fare.domain;

import org.springframework.stereotype.Component;

@Component
public class AgeFarePolicy {
    private static final Fare DEDUCTIBLE_FARE = Fare.from(350);
    private static final int CHILDREN_MIN_AGE = 6;
    private static final int CHILDREN_TEENAGER_BOUNDARY_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 19;

    public Fare calculate(Fare fare, int age) {
        if (isChildren(age)) {
            Fare discountAmount = fare.minus(DEDUCTIBLE_FARE).discountPercent(50);
            return fare.minus(discountAmount);
        }
        if (isTeenager(age)) {
            Fare discountAmount = fare.minus(DEDUCTIBLE_FARE).discountPercent(20);
            return fare.minus(discountAmount);
        }
        return fare;
    }

    private boolean isTeenager(int age) {
        return age >= CHILDREN_TEENAGER_BOUNDARY_AGE && age < TEENAGER_MAX_AGE;
    }

    private boolean isChildren(int age) {
        return age >= CHILDREN_MIN_AGE && age < CHILDREN_TEENAGER_BOUNDARY_AGE;
    }
}
