package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum AgeFarePolicy {
    CHILD(6, 12, fare -> (int) ((fare - 350) * 0.5)),
    YOUTH(13, 18, fare -> (int) ((fare - 350) * 0.8)),
    BASIC(0, 5, fare -> fare);

    private final int startAge;
    private final int endAge;
    private final Function<Integer, Integer> expression;

    AgeFarePolicy(int startAge, int endAge, Function<Integer, Integer> expression) {
        this.startAge = startAge;
        this.endAge = endAge;
        this.expression = expression;
    }

    public static AgeFarePolicy findByAge(int age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(ageFarePolicy -> ageFarePolicy.isIncludedRange(age))
                .findFirst()
                .orElse(BASIC);
    }

    private boolean isIncludedRange(int age) {
        return age >= startAge && age <= endAge;
    }

    public int calculate(int fare) {
        return expression.apply(fare);
    }
}
