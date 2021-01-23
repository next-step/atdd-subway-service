package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.function.Function;

enum AgeTypeFare {
    CHILDREN_FARE(450,0.5, age -> age >= 6 && age < 13),
    TEENAGER_FARE(720,0.8, age -> age >= 13 && age < 19),
    ADULT_FARE(1_250,1.0, age -> age >= 19),
    FREE_FARE(0,0, age -> age < 6 || age >= 65);

    private int defaultFare;
    private double discountRate;
    private Function<Integer, Boolean> ageType;

    AgeTypeFare(int defaultFare,double discountRate, Function<Integer, Boolean> ageType) {
        this.defaultFare = defaultFare;
        this.discountRate = discountRate;
        this.ageType = ageType;
    }

    public static AgeTypeFare valueOf(int age) {
        return Arrays.stream(AgeTypeFare.values())
                .filter(it -> it.isAgeType(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }

    public boolean isAgeType(int age) {
        return ageType.apply(age);
    }

    public BigDecimal caculate(BigDecimal overFare) {
        BigDecimal fare = BigDecimal.valueOf(this.defaultFare);
        BigDecimal discountedOverFare = overFare.multiply(BigDecimal.valueOf(discountRate));
        return fare.add(discountedOverFare);
    }

}
