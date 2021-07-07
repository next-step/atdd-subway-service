package nextstep.subway.path.domain;

import java.util.Arrays;

public enum AgeFarePolicy {
    CHILD(6, 13, 0.5, 350),
    TEENAGER(13, 19, 0.8, 350),
    ADULT(19, Integer.MAX_VALUE, 1.0, 0);

    private int minAge;
    private int maxAge;
    private double discountRate;
    private int deduction;

    AgeFarePolicy(int minAge, int maxAge, double discountRate, int deduction) {
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.discountRate = discountRate;
        this.deduction = deduction;
    }

    static AgeFarePolicy find(int age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(it -> it.isTarget(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean isTarget(int age) {
        return age >= minAge && age < maxAge;
    }

    public int calculate(int fare) {
        return (int) ((fare - deduction) * discountRate);
    }
}
