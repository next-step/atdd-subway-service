package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.User;

public class AgeFare {

    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final int DEFAULT_DISCOUNT_FARE = 350;

    private AgeFare() {
    }

    public static int calculateAgeFare(int fare, User user) {
        if (!user.isLoginUser() || !(user.isTeenager() || user.isChildren())) {
            return fare;
        }
        if (user.isTeenager()) {
            return discountAmountByAge(fare, TEENAGER_DISCOUNT_RATE);
        }
        return discountAmountByAge(fare, CHILDREN_DISCOUNT_RATE);
    }

    private static int discountAmountByAge(int fare, double discountRate) {
        return (int) ((fare - DEFAULT_DISCOUNT_FARE) * (1-discountRate));
    }
}
