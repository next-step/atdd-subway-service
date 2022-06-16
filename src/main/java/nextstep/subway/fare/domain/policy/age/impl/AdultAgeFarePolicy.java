package nextstep.subway.fare.domain.policy.age.impl;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public class AdultAgeFarePolicy implements AgeFarePolicy {

    @Override
    public int calculate(int age) {
        return defaultFare;
    }

    @Override
    public boolean isAge(int age) {
        return age >= 19 && age < 65;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

}
