package nextstep.subway.fare.domain.policy.age.impl;

import nextstep.subway.fare.domain.policy.age.AgeFarePolicy;

public class ChildAgeFarePolicy implements AgeFarePolicy {

    private final double discountRate = 50;
    private final int discountFare = 350;

    @Override
    public int calculate(int age) {
        return (int) ((defaultFare - discountFare) * (100 - discountRate) / 100);
    }

    @Override
    public boolean isAge(int age) {
        return age >= 6 && age < 13;
    }

    @Override
    public double getDiscountRate() {
        return discountRate;
    }

}
