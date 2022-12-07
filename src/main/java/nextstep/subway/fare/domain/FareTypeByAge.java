package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum FareTypeByAge {
    NON_MEMBER(age -> age == -1, basicFare -> 0),
    ADULT(age -> age >= 19, basicFare -> 0),
    TEENAGER(age -> age >= 13 && age < 19, basicFare -> (int) Math.ceil((basicFare - 350) * 0.2)),
    KIDS(age -> age >= 6 && age < 13, basicFare -> (int) Math.ceil((basicFare - 350) * 0.5)),
    TODDLER(age -> age >= 1 && age < 6, basicFare -> 0);

    private final Predicate<Integer> standard;
    private final Function<Integer, Integer> formula;

    FareTypeByAge(Predicate<Integer> standard, Function<Integer, Integer> formula) {
        this.standard = standard;
        this.formula = formula;
    }

    public static Integer calculate(int age, int basicFare){
        return Arrays.stream(values())
                .filter(fareTypeByAge -> fareTypeByAge.standard.test(age))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new)
                .formula.apply(basicFare);
    }
}
