package nextstep.subway.fare.domain;

import nextstep.subway.auth.domain.Age;

import java.util.Objects;

public enum DiscountType {
    NONE(0, 0),
    YOUTH(350, 0.8),
    CHILD(350, 0.5);

    private final int fare;
    private final double discountPercent;

    DiscountType(int fare, double discountPercent) {
        this.fare = fare;
        this.discountPercent = discountPercent;
    }

    public static DiscountPolicy ofDiscountPolicy(Age age) {
        if (Objects.isNull(age)) {
            return new GeneralDiscountPolicy();
        }

        if (age.isChild()) {
            return new ChildDiscountPolicy();
        }

        if (age.isYouth()) {
            return new YouthDiscountPolicy();
        }

        return new GeneralDiscountPolicy();
    }

    public int getFare() {
        return fare;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }
}
