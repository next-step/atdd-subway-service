package nextstep.subway.path.domain;

import java.util.stream.Stream;

public enum DiscountPolicy {
    BABY(6, 0.0),
    CHILD(13, 0.5),
    KIDS(19, 0.7),
    ADULT(999, 1.0);

    private final double rate;
    private final double age;

    DiscountPolicy(double age, double rate) {
        this.age = age;
        this.rate = rate;
    }

    public static DiscountPolicy of(int age) {
        return Stream.of(values())
            .filter(it -> it.is(age))
            .findFirst()
            .orElse(ADULT);
    }

    public double reduce(double fare) {
        return fare * rate;
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
