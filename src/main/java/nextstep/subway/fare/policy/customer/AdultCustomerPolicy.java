package nextstep.subway.fare.policy.customer;

import org.springframework.stereotype.Component;

import nextstep.subway.member.domain.Member;
import nextstep.subway.fare.domain.Fare;

@Component
public class AdultCustomerPolicy extends CustomerPolicy {

    public static final int MINIMUM_AGE = 20;

    private AdultCustomerPolicy() {
        addPolicy(this);
    }

    @Override
    public Fare apply(Fare fare) {
        return fare;
    }

    @Override
    public boolean isAvailable(Member member) {
        return MINIMUM_AGE <= member.getAge();
    }
}
