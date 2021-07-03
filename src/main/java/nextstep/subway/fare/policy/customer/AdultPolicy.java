package nextstep.subway.fare.policy.customer;

import nextstep.subway.fare.domain.Fare;
import nextstep.subway.member.domain.Member;

public class AdultPolicy implements CustomerPolicy {

    public static final int MINIMUM_AGE = 20;

    AdultPolicy() {
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
