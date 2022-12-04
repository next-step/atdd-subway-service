package nextstep.subway.path.fare.policy;

import nextstep.subway.auth.domain.LoginMember;
import nextstep.subway.path.fare.Fare;
import org.springframework.stereotype.Component;

@Component
public class AgeDiscountFarePolicy implements DiscountFarePolicy {

    public static final Fare BASIC_DISCOUNT_FARE = Fare.valueOf(350);
    public static final double CHILDREN_DISCOUNT_PERCENT = 0.5;
    public static final double TEENAGER_DISCOUNT_PERCENT = 0.8;
    public static final int MIN_CHILDREN_AGE = 6;
    public static final int MAX_CHILDREN_AGE = 12;
    public static final int MIN_TEEN_AGE = 13;
    public static final int MAX_TEEN_AGE = 18;

    @Override
    public Fare discount(LoginMember loginMember, Fare fare) {
        if (LoginMember.ANONYMOUS == loginMember) {
            return fare;
        }
        return discount(loginMember.getAge(), fare);
    }

    private Fare discount(int age, Fare fare) {
        if (isTeenager(age)) {
            return applyTeenagerDiscount(fare);
        }
        if (isChildren(age)) {
            return applyChildrenDiscount(fare);
        }

        return fare;
    }

    private Fare applyChildrenDiscount(Fare fare) {
        return fare.minus(BASIC_DISCOUNT_FARE).multiply(CHILDREN_DISCOUNT_PERCENT);
    }

    private Fare applyTeenagerDiscount(Fare fare) {
        return fare.minus(BASIC_DISCOUNT_FARE).multiply(TEENAGER_DISCOUNT_PERCENT);
    }

    private boolean isChildren(int age) {
        return age >= MIN_CHILDREN_AGE && age <= MAX_CHILDREN_AGE;
    }

    private boolean isTeenager(int age) {
        return age >= MIN_TEEN_AGE && age <= MAX_TEEN_AGE;
    }
}
