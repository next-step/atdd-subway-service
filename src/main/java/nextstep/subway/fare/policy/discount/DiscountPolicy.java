package nextstep.subway.fare.policy.discount;

import nextstep.subway.fare.policy.extra.ExtraFarePolicy;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class DiscountPolicy {
    private List<ExtraFarePolicy> extraFarePolicies;
    protected int age;

    public DiscountPolicy(int age, ExtraFarePolicy... extraFarePolicies) {
        this.age = age;
        this.extraFarePolicies = Arrays.asList(extraFarePolicies);
    }

    public int calculateDiscountAmount(int fare, int extraFare) {
        AtomicInteger result = new AtomicInteger(fare);

        extraFarePolicies.stream()
                .filter(policy -> validateAge(age))
                .findFirst()
                .ifPresent(policy -> {
                    result.set(getDiscountAmount(fare + policy.addExtraFee(extraFare)));
                });
        return result.get();
    }

    protected abstract int getDiscountAmount(int fare);

    protected abstract boolean validateAge(int age);
}
