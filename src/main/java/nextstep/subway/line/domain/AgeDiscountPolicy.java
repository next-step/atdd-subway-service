package nextstep.subway.line.domain;

import java.util.Arrays;

public enum AgeDiscountPolicy {
    CHILD(6, 12, 350, 0.5),
    TEENAGER(13, 18, 350, 0.2),
    BASIC(0, 999, 0, 0);

    private final int minAge;
    private final int maxAge;
    private final int fixDiscount;
    private final double ratioDiscount;

    AgeDiscountPolicy(int minAge, int maxAge, int fixDiscount, double ratioDiscount) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.fixDiscount = fixDiscount;
        this.ratioDiscount = ratioDiscount;
    }

    public static AgeDiscountPolicy of(int age) {
        return Arrays.stream(AgeDiscountPolicy.values())
                .filter(it -> it.isCorrectAge(age))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("나이가 유효하지 않습니다."));

    }

    public Fee calculate(Fee fee) {
        return discount(fee);
    }

    private Fee discount(Fee fee) {
        return Fee.of((int) ((fee.get() - fixDiscount) * (1 - ratioDiscount)));
    }

    private boolean isCorrectAge(int age) {
        return minAge <= age && age <= maxAge;
    }
}
