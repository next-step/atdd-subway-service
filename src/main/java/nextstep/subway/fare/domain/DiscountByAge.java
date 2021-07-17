package nextstep.subway.fare.domain;

import nextstep.subway.member.domain.Age;

import java.math.BigDecimal;
import java.util.Arrays;

import static java.math.BigDecimal.ONE;

public enum DiscountByAge {
    CHILD_DISCOUNT(new Age(6), new Age(13), new Rate(BigDecimal.valueOf(0.5D))),
    TEEN_DISCOUNT(new Age(13), new Age(19), new Rate(BigDecimal.valueOf(0.8D)));

    public static final Rate DEFAULT_RATE = new Rate(ONE);
    public static final Fare DEDUCTION = new Fare(350);

    private Age moreAge;
    private Age lessAge;
    private Rate discountRate;

    DiscountByAge(Age moreAge, Age lessAge, Rate discountRate) {
        this.moreAge = moreAge;
        this.lessAge = lessAge;
        this.discountRate = discountRate;
    }

    public static Fare calculate(Fare base, Age age) {
        Rate discountRate = findRate(age);

        return base.subtract(DEDUCTION)
                .multiply(discountRate);
    }

    private static Rate findRate(Age age) {
        return Arrays.stream(values())
                .filter(value -> age.isBetween(value.moreAge, value.lessAge))
                .map(value -> value.discountRate)
                .findFirst().orElse(DEFAULT_RATE);
    }
}
