package nextstep.subway.fare.policy.customer;

import org.springframework.stereotype.Component;

import nextstep.subway.member.domain.Member;
import nextstep.subway.fare.domain.Fare;

@Component
public class ChildPolicy extends CustomerPolicy {

    private static final int MINIMUM_AGE = 6;
    private static final int MAXIMUM_AGE = 13;

    private static final int FIXED_DISCOUNT = 350;
    private static final double PAYMENT_RATIO = 0.5;

    private ChildPolicy() {
        addPolicy(this);
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
