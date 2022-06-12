package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.util.Arrays;

public enum AgeDiscountPolicy {
    OLD(65, 100),
    ADULT(19, 0),
    TEENAGER(13, 20),
    CHILDREN(6, 50),
    TODDLER(0, 100);

    private static final Fare DEFAULT_MINUS_FARE = new Fare(350);

    private final int age;
    private final double discount;

    AgeDiscountPolicy(int age, int discount) {
        this.age = age;
        this.discount = discount;
    }

    public static AgeDiscountPolicy of(int age) {
        return Arrays.stream(values())
                     .filter(it -> it.age <= age)
                     .findFirst()
                     .orElseThrow(IllegalArgumentException::new);
    }

    public Fare discount(Fare fare) {
        if (discount == 0) {
            return fare;
        }
        return calculate(fare);
    }

    private Fare calculate(Fare fare) {
        fare = fare.minus(DEFAULT_MINUS_FARE);
        int fareValue = fare.getValue().intValue();
        Fare discountFare = new Fare((int) (fareValue * (discount / 100)));
        return fare.minus(discountFare);
    }
}
