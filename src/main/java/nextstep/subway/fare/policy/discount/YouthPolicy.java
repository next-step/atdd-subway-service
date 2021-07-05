package nextstep.subway.fare.policy.discount;

import nextstep.subway.fare.policy.extra.ExtraFarePolicy;

public class YouthPolicy extends DiscountPolicy {
    private static final double DISCOUNT_PERCENT = 80.0 / 100;
    private static final int BASIC_RATE = 350;
    private static final int YOUTH_MINIMUM_AGE = 13;
    private static final int YOUTH_MAXIMUM_AGE = 20;

    public YouthPolicy(int age, ExtraFarePolicy... extraFarePolicies) {
        super(age, extraFarePolicies);
    }

    @Override
    protected int getDiscountAmount(int fare) {
        return (int) ((fare - BASIC_RATE) * DISCOUNT_PERCENT);
    }

    @Override
    protected boolean validateAge(int age) {
        return age >= YOUTH_MINIMUM_AGE && age < YOUTH_MAXIMUM_AGE;
    }
}
