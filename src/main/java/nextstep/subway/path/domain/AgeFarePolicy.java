package nextstep.subway.path.domain;

import java.util.function.Function;

public enum AgeFarePolicy {
    ALL(value -> value),
    TEENAGER(value -> (int)(Math.ceil((value - 350) * 4 / 5.0))),
    CHILDREN(value -> (int)(Math.ceil((value - 350) / 2.0))),
    FREE(value -> 0);

    private Function<Integer, Integer> operator;

    AgeFarePolicy(Function<Integer, Integer> operator) {
        this.operator = operator;
    }

    public static int calculate(int fare, int age) {
        if(age < 6 || age >= 65) {
            return FREE.operator.apply(fare);
        }

        if(age >= 6 && age < 13) {
            return CHILDREN.operator.apply(fare);
        }

        if(age >= 13 && age < 19) {
            return TEENAGER.operator.apply(fare);
        }

        return ALL.operator.apply(fare);
    }

    public Function<Integer, Integer> getOperator() {
        return operator;
    }
}
