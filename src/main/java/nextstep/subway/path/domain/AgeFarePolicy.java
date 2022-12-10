package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeFarePolicy {
    OLD(65, Integer.MAX_VALUE, 100),
    ADULT(19, 64,0),
    TEENAGER(13, 18, 20),
    CHILDREN(6, 12, 50),
    TODDLER(0, 5,100);

    private static final int DEFAULT_MINUS_FARE = 350;

    private final int minimumAge;
    private final int maximumAge;
    private final double discount;

    AgeFarePolicy(int minimumAge, int maximumAge, double discount) {
        this.minimumAge = minimumAge;
        this.maximumAge = maximumAge;
        this.discount = discount;
    }

    public static AgeFarePolicy of(int age) {
        return Arrays.stream(values())
            .filter(it -> it.checkAgeRange(age))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    private boolean checkAgeRange(int age) {
        return this.minimumAge <= age && this.maximumAge >= age;
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

