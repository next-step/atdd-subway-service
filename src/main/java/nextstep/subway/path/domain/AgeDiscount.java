package nextstep.subway.path.domain;

import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscount {

    CHILD(age -> age < 13, fare -> (int) ((fare - 350) * 0.5)),
    YOUTH(age -> age < 19, fare -> (int) ((fare - 350) * 0.8));

    private Predicate<Integer> predicate;
    private Function<Integer, Integer> function;

    AgeDiscount(Predicate<Integer> predicate, Function<Integer, Integer> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public boolean condition(int age) {
        return this.predicate.test(age);
    }

    public int execute(int fare) {
        return this.function.apply(fare);
    }

    public static int discount(int age, int fare) {
        return Arrays.stream(values())
                .filter(c -> c.condition(age))
                .map(c -> c.execute(fare))
                .findFirst()
                .orElse(fare);
    }
}
