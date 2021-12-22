package nextstep.subway.path.domain;

import java.util.stream.Stream;

public enum DiscountPolicy {
    BABY(6, 0.0, 0),
    CHILD(13, 0.5, 350),
    KIDS(19, 0.7, 350),
    ADULT(999, 1.0, 0);

    private final int age;
    private final double rate;
    private final int discountAmount;

    DiscountPolicy(int age, double rate, int discountAmount) {
        this.age = age;
        this.rate = rate;
        this.discountAmount = discountAmount;
    }

    public static DiscountPolicy of(int age) {
        return Stream.of(values())
            .filter(it -> it.is(age))
            .findFirst()
            .orElse(ADULT);
    }

    public double reduce(int fare) {
        return (fare - discountAmount) * rate;
    }

    private boolean is(int age) {
        return this.age > age;
    }

    public double getRate() {
        return rate;
    }

    public double getAge() {
        return age;
    }
}
