package nextstep.subway.common.domain;

import java.util.Arrays;

public enum UserFarePolicy {
    BABY(0, 5, 0.0, 0),
    CHILD(6, 12, 0.5, 350),
    TEENAGER(13, 18, 0.8, 350),
    ADULT(19, Integer.MAX_VALUE, 1.0, 0);

    private final int baseAge;
    private final int topAge;
    private final Double discountRate;
    private final int discountFare;

    UserFarePolicy(int baseAge, int topAge, Double discountRate, int discountFare) {
        this.baseAge = baseAge;
        this.topAge = topAge;
        this.discountRate = discountRate;
        this.discountFare = discountFare;
    }

    public static boolean isBaby(int age) {
        return BABY.isIncluding(age);
    }

    public static boolean isChild(int age) {
        return CHILD.isIncluding(age);
    }

    public static boolean isTeenager(int age) {
        return TEENAGER.isIncluding(age);
    }

    public static boolean isAdult(int age) {
        return ADULT.isIncluding(age);
    }

    private boolean isIncluding(int age) {
        return this.baseAge <= age && age <= this.topAge;
    }

    public static Double findDiscountRate(int age) {
        return Arrays.stream(UserFarePolicy.values())
                .filter(userFarePolicy -> userFarePolicy.isIncluding(age))
                .findFirst()
                .map(userFarePolicy -> userFarePolicy.discountRate)
                .orElse(ADULT.discountRate);
    }

    public static int findDiscountFare(int age) {
        return Arrays.stream(UserFarePolicy.values())
                .filter(userFarePolicy -> userFarePolicy.isIncluding(age))
                .findFirst()
                .map(userFarePolicy -> userFarePolicy.discountFare)
                .orElse(ADULT.discountFare);
    }
}
