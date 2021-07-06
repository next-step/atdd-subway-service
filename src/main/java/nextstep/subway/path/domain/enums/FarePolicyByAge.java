package nextstep.subway.path.domain.enums;

import nextstep.subway.path.domain.FarePolicy;

import java.util.Arrays;
import java.util.function.DoubleFunction;
import java.util.function.Predicate;

public enum FarePolicyByAge implements FarePolicy {
    ADULT(age -> age >= 19, fare -> fare),
    TEENAGER(age -> age >= 13 && age < 19, fare -> (fare - 350) * 0.8),
    CHILDREN(age -> age >= 6 && age < 13, fare -> (fare - 350) * 0.5),
    BABY(age -> age >= 0 && age < 6, age -> Double.valueOf(0));
    private final Predicate<Integer> predicate;
    private final DoubleFunction<Double> function;

    FarePolicyByAge(Predicate<Integer> predicate, DoubleFunction<Double> function) {
        this.predicate = predicate;
        this.function = function;
    }

    public static FarePolicyByAge findCategory(int age) {
        return Arrays.stream(values())
                .filter(farePolicy -> farePolicy.predicate.test(age))
                .findFirst()
                .get();
    }

    @Override
    public double calculate(double fare) {
        return function.apply(fare);
    }
}
