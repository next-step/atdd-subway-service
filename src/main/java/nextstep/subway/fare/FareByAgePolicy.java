package nextstep.subway.fare;

import java.util.*;
import java.util.function.*;

public enum FareByAgePolicy {
    INFANT(age -> agePolicyCondition(FareByAgePolicy.INFANT_AGE_MIN, FareByAgePolicy.INFANT_AGE_MAX, age),
        fare -> Fare.from(0)),

    KIDS(age -> agePolicyCondition(FareByAgePolicy.KIDS_AGE_MIN, FareByAgePolicy.KIDS_AGE_MAX, age),
        fare -> fare.minus(FareByAgePolicy.DEDUCTIBLE_FARE).multiply(FareByAgePolicy.KIDS_FARE_ASSIGN_RATE).round()),

    YOUTH(age -> agePolicyCondition(FareByAgePolicy.YOUTH_AGE_MIN, FareByAgePolicy.YOUTH_AGE_MAX, age),
        fare -> fare.minus(FareByAgePolicy.DEDUCTIBLE_FARE).multiply(FareByAgePolicy.YOUTH_FARE_ASSIGN_RATE).round()),

    GENERAL(age -> agePolicyCondition(FareByAgePolicy.GENERAL_AGE_MIN, FareByAgePolicy.GENERAL_AGE_MAX, age),
        fare -> fare);

    private static final String AGE_VALUE_IS_WRONG_EXCEPTION_STATEMENT = "나이값이 잘못되었습니다.";

    private static final int DEDUCTIBLE_FARE = 350;
    private static final int INFANT_AGE_MIN = 0;
    private static final int INFANT_AGE_MAX = 5;
    private static final int KIDS_AGE_MIN = 6;
    private static final int KIDS_AGE_MAX = 12;
    private static final int YOUTH_AGE_MIN = 13;
    private static final int YOUTH_AGE_MAX = 18;
    private static final int GENERAL_AGE_MIN = 19;
    private static final int GENERAL_AGE_MAX = 200;

    private static final double YOUTH_FARE_ASSIGN_RATE = 0.8;
    private static final double KIDS_FARE_ASSIGN_RATE = 0.5;

    private final Predicate<Integer> ageCondition;
    private final Function<Fare, Fare> function;

    FareByAgePolicy(Predicate<Integer> ageCondition, Function<Fare, Fare> function) {
        this.ageCondition = ageCondition;
        this.function = function;
    }

    public static boolean agePolicyCondition(int min, int max, int age) {
        return min <= age && age <= max;
    }

    public static Fare calculateFare(int age, Fare fare) {
        Fare fareByAge = Arrays.stream(values())
            .filter(value -> value.ageCondition.test(age))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(AGE_VALUE_IS_WRONG_EXCEPTION_STATEMENT))
            .function.apply(fare);
        return fareByAge;
    }
}
