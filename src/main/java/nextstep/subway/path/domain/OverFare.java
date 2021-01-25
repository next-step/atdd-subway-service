package nextstep.subway.path.domain;

import java.util.function.Function;

public enum OverFare {
    FIVE_TRUE(distance  -> (int) ((Math.floor((distance - 1) / 5) + 1) * 100)),
    FIVE_FALSE(distance -> (int) (Math.floor((distance - 1) / 5) * 100)),
    EIGHT_TRUE(distance -> (int) ((Math.floor((distance - 1) / 8) + 1) * 100)),
    EIGHT_FALSE(distance -> (int) (Math.floor((distance -1 ) / 8) * 100));

    public Function<Integer, Integer> expression;
    OverFare(Function<Integer, Integer> expression) {
        this.expression = expression;
    }

    public int calculate(int distance) { return expression.apply(distance);}
}
