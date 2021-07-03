package nextstep.subway.fare.policy.customer;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.domain.Member;

public class TeenagerPolicy implements CustomerPolicy {

    private static final int MINIMUM_AGE = 13;
    private static final int MAXIMUM_AGE = 20;

    private static final int FIXED_DISCOUNT = 350;
    private static final double PAYMENT_RATIO = 0.8;

    TeenagerPolicy() {
    }

    @Override
    public Fare apply(Fare fare) {
        return fare
            .subtract(FIXED_DISCOUNT)
            .multiplyBy(PAYMENT_RATIO);
    }

    @Override
    public boolean isAvailable(Member member) {
        return MINIMUM_AGE <= member.getAge() && member.getAge() < MAXIMUM_AGE;
    }
}
