package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeFarePolicy {
    INFANT(0, 6, fare -> 0),
    KIDS(6, 13, fare -> (fare - 350) * 5 / 10),
    TEENAGER(13, 19, fare -> (fare - 350) * 8 / 10),
    ADULT(20, Integer.MAX_VALUE, fare -> fare);


    private final int beginAge;
    private final int endAge;
    private final Function<Integer, Integer> discount;

    AgeFarePolicy(final int beginAge, final int endAge, final Function<Integer, Integer> discount) {
        this.beginAge = beginAge;
        this.endAge = endAge;
        this.discount = discount;
    }

    public static int ofDiscount(final int age, final int fare) {
        return Arrays.stream(values())
                .filter(it -> it.includeScope(age))
                .findFirst()
                .orElse(ADULT)
                .discount(fare);
    }

    public int discount(final int fare) {
        return discount.apply(fare);
    }

    boolean includeScope(final int age) {
        return (age >= beginAge && age < endAge);
    }

    @Override
    public String toString() {
        return "AgeFarePolicy{" +
                "beginAge=" + beginAge +
                ", endAge=" + endAge +
                '}';
    }
}
