package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeFarePolicy {
    ALL(value -> value, age -> age >= 19 && age < 65),
    TEENAGER(value -> (int)(Math.ceil((value - 350) * 4 / 5.0)), age -> age >= 13 && age < 19),
    CHILDREN(value -> (int)(Math.ceil((value - 350) / 2.0)), age -> age >= 6 && age < 13),
    FREE(value -> 0, age -> age < 6 || age >= 65);

    private Function<Integer, Integer> operator;
    private Predicate<Integer> agePredicate;

    AgeFarePolicy(Function<Integer, Integer> operator, Predicate<Integer> agePredicate) {
        this.operator = operator;
        this.agePredicate = agePredicate;
    }

    public static int calculate(int fare, int age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(ageFarePolicy -> ageFarePolicy.agePredicate.test(age))
                .findFirst()
                .map(ageFarePolicy -> ageFarePolicy.operator.apply(fare))
                .orElseThrow(IllegalArgumentException::new);
    }

    public Function<Integer, Integer> getOperator() {
        return operator;
    }
}
