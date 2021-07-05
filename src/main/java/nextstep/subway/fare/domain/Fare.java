package nextstep.subway.fare.domain;

import java.util.List;

import nextstep.subway.fare.policy.FarePolicy;

public class Fare {

    public static final Fare DEFAULT = new Fare(1250);

    private final int value;

    public Fare(int value) {
        this.value = value;
    }

    public Fare add(int amount) {
        return new Fare(value + amount);
    }

    public Fare subtract(int amount) {
        return new Fare(value - amount);
    }

    public Fare multiplyBy(double ratio) {
        return new Fare((int) (value * ratio));
    }

    public int getValue() {
        return value;
    }

    public Fare apply(FarePolicy policy) {
        return policy.calculate(this);
    }

    public Fare apply(List<FarePolicy> policies) {
        Fare fare = this;

        for (FarePolicy policy : policies) {
            fare = fare.apply(policy);
        }

        return fare;
    }
}
