package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeFarePolicy {
    OLD(65, 100),
    ADULT(19, 0),
    TEENAGER(13, 20),
    CHILDREN(6, 50),
    TODDLER(0, 100);

    private static final int DEFAULT_MINUS_FARE = 350;

    private final int age;
    private final double discount;

    AgeFarePolicy(int age, int discount) {
        this.age = age;
        this.discount = discount;
    }

    public static AgeFarePolicy of(int age) {
        return Arrays.stream(values())
            .filter(it -> it.age <= age)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public int discount(int fare) {
        if (discount == 0) {
            return fare;
        }
        return calculate(fare);
    }

    private int calculate(int fare) {
        fare -= DEFAULT_MINUS_FARE;
        int discountFare = (int) (fare * (discount / 100));
        return fare - discountFare;
    }
}

