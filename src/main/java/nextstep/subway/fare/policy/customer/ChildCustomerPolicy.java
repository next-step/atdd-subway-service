package nextstep.subway.fare.policy.customer;

import org.springframework.stereotype.Component;

import nextstep.subway.member.domain.Member;
import nextstep.subway.fare.domain.Fare;

@Component
public class ChildCustomerPolicy extends CustomerPolicy {

    private static final int MINIMUM_AGE = 6;
    private static final int MAXIMUM_AGE = 13;

    private static final int CHILD_DISCOUNT = 350;
    private static final double PAY_RATE = 0.5;

    private ChildCustomerPolicy() {
        addPolicy(this);
    }

    @Override
    public Fare apply(Fare fare) {
        return Fare.of((fare.getValue() - CHILD_DISCOUNT) * PAY_RATE);
    }

    @Override
    public boolean isAvailable(Member member) {
        return MINIMUM_AGE <= member.getAge() && member.getAge() < MAXIMUM_AGE;
    }
}
