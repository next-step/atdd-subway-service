package nextstep.subway.domain.path.domain;

import nextstep.subway.domain.auth.domain.User;

public class AgeFare {

    private static final double TEENAGER_DISCOUNT_RATE = 0.2;
    private static final double CHILDREN_DISCOUNT_RATE = 0.5;
    private static final int DEFAULT_DISCOUNT_FARE = 350;
    private static final int AMOUNT_ZERO = 0;
    private User user;

    AgeFare() {
    }

    AgeFare(User user) {
        this.user = user;
    }

    public Amount calculateDiscount(Amount amount) {
        if (!user.isLoginUser() || isAdult()) {
            return new Amount(AMOUNT_ZERO);
        }
        if (user.isTeenager()) {
            return new Amount(discountAmountByAge(amount, TEENAGER_DISCOUNT_RATE));
        }
        return new Amount(discountAmountByAge(amount, CHILDREN_DISCOUNT_RATE));
    }

    private boolean isAdult() {
        return !(user.isTeenager() || user.isChildren());
    }

    private int discountAmountByAge(Amount amount, double discountRate) {
        return (int) (amount.getAmount() - ((amount.getAmount() - DEFAULT_DISCOUNT_FARE) * (1-discountRate)));
    }
}