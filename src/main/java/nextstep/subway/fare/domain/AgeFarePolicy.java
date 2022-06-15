package nextstep.subway.fare.domain;

import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Predicate;
import nextstep.subway.member.domain.Age;

public enum AgeFarePolicy {
    TEENAGER_POLICY(AgeFarePolicy::isInTeenagerRange, AgeFarePolicy::discountTeenagerFare),
    CHILDREN_POLICY(AgeFarePolicy::isInChildrenRange, AgeFarePolicy::discountChildrenFare),
    NONE_POLICY(age -> false, fare -> {});

    private static final int CHILDREN_MIN = 6;
    private static final int TEENAGER_MIN = 13;
    private static final int TEENAGER_MAX = 19;
    private static final int DEDUCTIBLE_FARE = 350;
    private static final int TEENAGER_DISCOUNTED_PERCENTAGE = 80;
    private static final int CHILDREN_DISCOUNTED_PERCENTAGE = 50;
    public static final int ONE_HUNDRED = 100;
    private final Predicate<Age> predicateByAge;
    private final Consumer<Fare> discountFare;

    AgeFarePolicy(Predicate<Age> predicateByAge, Consumer<Fare> discountFare) {
        this.predicateByAge = predicateByAge;
        this.discountFare = discountFare;
    }

    private static boolean isInTeenagerRange(Age age) {
        return age.age() >= TEENAGER_MIN && age.age() < TEENAGER_MAX;
    }

    private static boolean isInChildrenRange(Age age) {
        return age.age() >= CHILDREN_MIN && age.age() < TEENAGER_MIN;
    }

    private static void discountTeenagerFare(Fare fare) {
        fare.minus(Fare.valueOf(DEDUCTIBLE_FARE));
        fare.multiply(TEENAGER_DISCOUNTED_PERCENTAGE);
        fare.divideBy(ONE_HUNDRED);
    }

    private static void discountChildrenFare(Fare fare) {
        fare.minus(Fare.valueOf(DEDUCTIBLE_FARE));
        fare.multiply(CHILDREN_DISCOUNTED_PERCENTAGE);
        fare.divideBy(ONE_HUNDRED);
    }

    public static AgeFarePolicy findAgeFarePolicyByAge(Age age) {
        return Arrays.stream(AgeFarePolicy.values())
                .filter(ageFarePolicy -> ageFarePolicy.predicateByAge.test(age))
                .findFirst()
                .orElse(AgeFarePolicy.NONE_POLICY);
    }

    public void discountFare(Fare fare) {
        this.discountFare.accept(fare);
    }
}
