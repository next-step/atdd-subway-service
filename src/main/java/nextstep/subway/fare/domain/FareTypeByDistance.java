package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FareTypeByDistance {
    TEN_UNDER(distance -> distance <= 10, distance -> 0),
    FIFTY_UNDER(distance -> distance <= 50, distance -> (int) ((Math.ceil((distance - 1) / 5) + 1) * 100)),
    FIFTY_OVER(distance -> distance > 50, distance -> (int) ((Math.ceil((distance - 1) / 8) + 1) * 100));

    private final Predicate<Integer> standard;
    private final Function<Integer, Integer> formula;

    FareTypeByDistance(Predicate<Integer> standard, Function<Integer, Integer> formula) {
        this.standard = standard;
        this.formula = formula;
    }

    public static Integer calculate(int distance){
        return Arrays.stream(values())
                .filter(fareTypeByAge -> fareTypeByAge.standard.test(distance))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .formula.apply(distance);
    }
}
