package nextstep.subway.member.domain;

import java.util.Arrays;

public enum AgeType {

    CHILDREN(6, 12, 0.5), TEENAGER(13, 18, 0.2), ADULT(19, Integer.MAX_VALUE, 0), BABY(0, 5, 0),
    ;

    private final Age startAge;
    public final Age endAge;

    public final double discountPercent;

    AgeType(int startAge, int endAge, double discountPercent) {
        this.startAge = Age.from(startAge);
        this.endAge = Age.from(endAge);
        this.discountPercent = discountPercent;
    }

    public static AgeType getAgeType(int age) {
        return Arrays.stream(values())
                .filter(ageType -> isBetweenAge(age, ageType))
                .findFirst().orElse(BABY);
    }

    private static boolean isBetweenAge(int age, AgeType ageType) {
        return ageType.startAge.getAge() <= age && ageType.endAge.getAge() >= age;
    }

    public double getDiscountPercent() {
        return discountPercent;
    }

    public boolean isDiscountTarget() {
        return this == CHILDREN || this == TEENAGER;
    }
}

