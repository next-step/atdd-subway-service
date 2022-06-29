package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.util.Arrays;

public enum AgeDiscountPolicy {
    ADULT(19, 0),
    TEENAGER(13, 20),
    CHILDREN(6, 50),
    INFANCY(0, 100);

    private static final Fare DEFAULT_MINUS_FARE = new Fare(350);

    private final int age;
    private final double discountRate;

    AgeDiscountPolicy(int age, int discountRate) {
        this.age = age;
        this.discountRate = discountRate;
    }

    public static Fare discount(Fare fare, int age) {
        fare.minus(DEFAULT_MINUS_FARE);

        AgeDiscountPolicy policy = valueOf(age);

        Fare discountFare = new Fare((int) (fare.getValue() * (policy.discountRate / 100)));
        fare.minus(discountFare);

        return fare;
    }

    private static AgeDiscountPolicy valueOf(int age) {
        return Arrays.stream(values())
                .filter(value -> value.age <= age)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
