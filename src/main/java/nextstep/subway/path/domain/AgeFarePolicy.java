package nextstep.subway.path.domain;

import java.math.BigDecimal;
import java.util.function.Function;

public enum AgeFarePolicy {
    ALL(value -> new BigDecimal(value)),
    TEENAGER(value -> new BigDecimal((value - 350) * 4 / 5).setScale(0, BigDecimal.ROUND_CEILING)),
    CHILDREN(value -> new BigDecimal((value - 350) / 2).setScale(0, BigDecimal.ROUND_CEILING)),
    Free(value -> new BigDecimal(0));

    private Function<Integer, BigDecimal> operator;

    AgeFarePolicy(Function<Integer, BigDecimal> operator) {
        this.operator = operator;
    }

    public static BigDecimal calculate(int fare, int age) {
        if(age < 6 || age >= 65) {
            return Free.operator.apply(fare);
        }

        if(age >= 6 && age < 13) {
            return CHILDREN.operator.apply(fare);
        }

        if(age >= 13 && age < 19) {
            return TEENAGER.operator.apply(fare);
        }

        return ALL.operator.apply(fare);
    }

    public Function<Integer, BigDecimal> getOperator() {
        return operator;
    }
}
