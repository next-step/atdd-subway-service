package nextstep.subway.line.domain;

import java.util.Arrays;

public enum FareByAge {
    ADULT(0, 0, 0, 0),
    TEENAGER(13, 19, 20, 350),
    CHILD(6, 13, 50, 350);

    private final int minAge;
    private final int maxAge;
    private final int discountRate;
    private final int discountBaseFare;

    private static final int DEFAULT_FARE = 1250;
    private static final int PERCENTAGE_UNIT = 100;

    FareByAge(int minAge, int maxAge, int discountRate, int discountBaseFare) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
        this.discountBaseFare = discountBaseFare;
    }

    public int fare() {
        return ((DEFAULT_FARE - discountBaseFare) * (PERCENTAGE_UNIT - discountRate)) / PERCENTAGE_UNIT;
    }

    public static FareByAge of(Integer age) {
        if (age == null) {
            return ADULT;
        }

        return Arrays.stream(values())
                .filter(fareByAge -> fareByAge.minAge <= age && fareByAge.maxAge > age)
                .findFirst()
                .orElse(ADULT);
    }
}
