package nextstep.subway.path.domain;

import nextstep.subway.line.domain.Fare;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.function.Function;
import java.util.function.Predicate;

public enum AgeDiscount {

    CHILD(age -> age >= 0 && age < 13, fare -> BigInteger.valueOf((long)((fare.longValue() - 350L) * 0.5))),
    YOUTH(age -> age > 13 && age < 19, fare -> BigInteger.valueOf((long)((fare.longValue() - 350L) * 0.8)));

    private Predicate<Integer> predicate;
    private Function<BigInteger, BigInteger> function;

    AgeDiscount(Predicate<Integer> predicate, Function<BigInteger, BigInteger> function) {
        this.predicate = predicate;
        this.function = function;
    }

    private boolean condition(int age) {
        return this.predicate.test(age);
    }

    private BigInteger execute(BigInteger fare) {
        return this.function.apply(fare);
    }

    public static Fare discount(int age, Fare fare) {
        BigInteger value = fare.getValue();
        return Arrays.stream(values())
                .filter(c -> c.condition(age))
                .map(c -> c.execute(value))
                .findFirst()
                .map(Fare::new)
                .orElse(fare);
    }
}
