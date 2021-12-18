package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

import static nextstep.subway.line.domain.Fare.*;
import static nextstep.subway.path.domain.AgeGeneration.CHILD_GENERATION;
import static nextstep.subway.path.domain.AgeGeneration.YOUTH_GENERATION;

public enum AgeDiscount {

    CHILD(age -> age < CHILD_GENERATION.getAge(), fare -> fare.minus(DEFAULT_DISCOUNT_PRICE).multiply(DEFAULT_CHILD_RATE)),
    YOUTH(age -> age > CHILD_GENERATION.getAge() && age < YOUTH_GENERATION.getAge(), fare -> fare.minus(DEFAULT_DISCOUNT_PRICE).multiply(DEFAULT_YOUTH_RATE));

    private Predicate<Integer> predicate;
    private Function<Fare, Fare> function;

    AgeDiscount(Predicate<Integer> predicate, Function<Fare, Fare> function) {
        this.predicate = predicate;
        this.function = function;
    }

    private boolean condition(int age) {
        return this.predicate.test(age);
    }

    private Fare execute(Fare fare) {
        return this.function.apply(fare);
    }

    public static Fare discount(int age, Fare fare) {
        return Arrays.stream(values())
                .filter(c -> c.condition(age))
                .map(c -> c.execute(fare))
                .findFirst()
                .orElse(fare);
    }
}
