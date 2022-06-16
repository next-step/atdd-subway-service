package nextstep.subway.fare.domain.policy.age.impl;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public class FreeAgeFarePolicy implements AgeFarePolicy {

    private final int discountRate = 100;

    @Override
    public int calculate() {
        return 0;
    }

    @Override
    public boolean isAge(int age) {
        return age < 6 || age >= 65;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

}
