package nextstep.subway.fare;

import nextstep.subway.auth.domain.LoginMember;
import org.springframework.stereotype.Component;

@Component
public class AgeFarePolicy {
    private static final Fare DEDUCTIBLE_FARE = new Fare(350);
    private static final int CHILDREN_MIN_AGE = 6;
    private static final int CHILDREN_TEENAGER_BOUNDARY_AGE = 13;
    private static final int TEENAGER_MAX_AGE = 19;

    public Fare calculate(Fare fare, LoginMember loginMember) {
        if (isChildren(loginMember.getAge())) {
            return fare.minus(DEDUCTIBLE_FARE).discountPercent(50);
        }
        if (isTeenager(loginMember.getAge())) {
            return fare.minus(DEDUCTIBLE_FARE).discountPercent(20);
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
