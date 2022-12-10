package nextstep.subway.path.domain.fare;

import java.util.stream.Stream;

public enum Discount {
    NO_DISCOUNT(0,0,0),
    TEENAGER_DISCOUNT(13, 19, 0.2),
    CHILD_DISCOUNT(6, 13, 0.5);

    private int downAge;
    private int upAge;
    private double discountRate;

    Discount(int downAge, int upAge, double discountRate) {
        this.downAge = downAge;
        this.upAge = upAge;
        this.discountRate = discountRate;
    }

    public static Discount getDiscountByAge(int age){
        return Stream.of(values())
                .filter(it-> it.isMatch(age))
                .findFirst()
                .orElse(NO_DISCOUNT);
    }

    private boolean isMatch(int age){
        return age>=downAge && age < upAge;
    }

    public double getRate() {
        return discountRate;
    }
}
