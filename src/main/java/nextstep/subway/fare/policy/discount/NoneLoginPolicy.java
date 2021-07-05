package nextstep.subway.fare.policy.discount;

import nextstep.subway.fare.policy.extra.ExtraFarePolicy;

public class NoneLoginPolicy extends DiscountPolicy {
    private static final int CHILD_MINIMUM_AGE = 6;
    private static final int YOUTH_MAXIMUM_AGE = 20;
    private static final int NON_LOGIN_AGE = 0;

    public NoneLoginPolicy(ExtraFarePolicy... extraFarePolicies) {
        super(NON_LOGIN_AGE, extraFarePolicies);
    }

    @Override
    protected int getDiscountAmount(int fare) {
        return fare;
    }

    @Override
    protected boolean validateAge(int age) {
        return age < CHILD_MINIMUM_AGE || age > YOUTH_MAXIMUM_AGE;
    }
}
