package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.User;

public class AgeFare {

    private static final int TEENAGER_MINIMUM_AGE = 13;
    private static final int TEENAGER_MAXIMUM_AGE = 18;
    private static final int CHILDREN_MINIMUM_AGE = 6;
    private static final int CHILDREN_MAXIMUM_AGE = 12;
    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final int DEFAULT_DISCOUNT_FARE = 350;

    private AgeFare() {
    }

    public static int calculateAgeFare(int fare, User user) {
        if (isTeenager(user)) {
            return discountAmountByAge(fare, TEENAGER_DISCOUNT_RATE);
        }
        if (isChildren(user)) {
            return discountAmountByAge(fare, CHILDREN_DISCOUNT_RATE);
        }
        return fare;
    }

    private static boolean isChildren(final User user) {
        return user.getAge() >= CHILDREN_MINIMUM_AGE && user.getAge() <= CHILDREN_MAXIMUM_AGE;
    }

    private static boolean isTeenager(final User user) {
        return user.getAge() >= TEENAGER_MINIMUM_AGE && user.getAge() <= TEENAGER_MAXIMUM_AGE;
    }

    private static int discountAmountByAge(int fare, double discountRate) {
        return (int) ((fare - DEFAULT_DISCOUNT_FARE) * (1-discountRate));
    }
}
