package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import org.apache.commons.lang3.Range;

public enum AgeDiscountFare {
    TODDLER(1, 5, fare -> 0),
    KIDS(6, 12, fare -> (fare - 350) * 5 / 10),
    TEENAGER(13, 19, fare -> (fare - 350) * 8 / 10),
    ADULT(20, Integer.MAX_VALUE, fare -> fare);

    private final Range<Integer> range;
    private final Function<Integer, Integer> expression;

    AgeDiscountFare(int minAge, int maxAge, Function<Integer, Integer> expression) {
        this.range = Range.between(minAge, maxAge);
        this.expression = expression;
    }

    public static Fare calculate(int age, int fare) {
        AgeDiscountFare policy = find(age);
        return new Fare(policy.expression.apply(fare));
    }

    private static AgeDiscountFare find(int age) {
        return Arrays.stream(values())
                .filter(ageFarePolicy -> ageFarePolicy.contains(age))
                .findFirst()
                .orElse(ADULT);
    }

    private boolean contains(int age) {
        return range.contains(age);
    }
}
