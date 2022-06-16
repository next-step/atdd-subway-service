package nextstep.subway.fare.domain.policy.age.impl;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public class TeenagerAgeFarePolicy implements AgeFarePolicy {

    private final double discountRate = 20;
    private final int discountFare = 350;

    @Override
    public int calculate() {
        return (int) ((defaultFare - discountFare) * (100 - discountRate) / 100);
    }

    @Override
    public boolean isAge(int age) {
        return age >= 13 && age < 19;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

}
