package nextstep.subway.fare.policy.customer;

import nextstep.subway.fare.policy.FarePolicy;

public class CustomerAgeDiscountFarePolicy {
    private final int minAge;
    private final int maxAge;

    private final FarePolicy farePolicy;

    public CustomerAgeDiscountFarePolicy(int minAge, int maxAge, FarePolicy farePolicy) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.farePolicy = farePolicy;
    }

    public boolean includes(int age) {
        return minAge <= age && age < maxAge;
    }

    public FarePolicy getFarePolicy() {
        return farePolicy;
    }
}
