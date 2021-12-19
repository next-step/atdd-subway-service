package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.User;

public class AgeFare {

    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;

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
        return user.getAge() >= 6 && user.getAge() < 13;
    }

    private static boolean isTeenager(final User user) {
        return user.getAge() >= 13 && user.getAge() < 19;
    }

    private static int discountAmountByAge(int fare, double discountRate) {
        return (int) ((fare - 350) * (1-discountRate));
    }
}
