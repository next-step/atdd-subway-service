package nextstep.subway.path.domain;

import java.util.stream.Stream;

public enum Discount {
    ADULT(19, 1),
    TEENAGER(13, 0.8),
    CHILDREN(6, 0.5),
    INFANTS(0, 0)
    ;

    private final int minimumAge;
    private final double discountRate;

    Discount(int minimumAge, double discountRate) {
        this.minimumAge = minimumAge;
        this.discountRate = discountRate;
    }

    public static Discount select(int age) {
        return Stream.of(Discount.values())
                .filter(it -> it.minimumAge <= age)
                .findFirst()
                .orElse(INFANTS);
    }

    public int accept(int payment) {
        return (int)(payment * discountRate);
    }
}
