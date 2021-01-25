package nextstep.subway.path.domain;

import java.util.function.Function;

public enum AgeDiscount {
    CHILD(targetFee -> targetFee - (int)Math.floor(targetFee * 0.5)),
    YOUTH(targetFee -> targetFee - (int)Math.floor(targetFee * 0.2));


    public Function<Integer, Integer> expression;
    AgeDiscount(Function<Integer, Integer> expression) {
        this.expression = expression;
    }

    public int calculateDiscount(int targetFee) { return expression.apply(targetFee);}

}
