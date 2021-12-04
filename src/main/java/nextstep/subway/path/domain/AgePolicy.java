package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum AgePolicy {

    INFANT(age -> age < 6, fare -> 0),
    CHILD(age -> age >= 6 && age < 13, fare -> (int) Math.round((fare - 350) * 0.5)),
    YOUTH(age -> age >= 13 && age < 19, fare -> (int) Math.round((fare - 350) * 0.8)),
    ADULT(age -> age >= 19, fare -> fare);

    private final Function<Integer, Boolean> condition;
    private final Function<Integer, Integer> operation;

    AgePolicy(Function<Integer, Boolean> condition, Function<Integer, Integer> operation) {
        this.condition = condition;
        this.operation = operation;
    }

    public Function<Integer, Boolean> getCondition() {
        return condition;
    }

    public static AgePolicy from(int age) {
        return Arrays.stream(values())
                .filter(it -> it.getCondition().apply(age))
                .findFirst()
                .orElse(ADULT);
    }

    public int calculateDiscountedFare(int fare) {
        return operation.apply(fare);
    }
}
