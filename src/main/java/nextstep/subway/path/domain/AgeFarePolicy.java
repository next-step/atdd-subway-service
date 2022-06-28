package nextstep.subway.path.domain;

import nextstep.subway.path.domain.policy.*;

import java.util.Arrays;
import java.util.function.Predicate;

public enum AgeFarePolicy {
    ALL(age -> (age >= 19 && age < 65) || age == 0, new AllFarePolicy()),
    TEENAGER(age -> age >= 13 && age < 19, new TeenagerFarePolicy()),
    CHILDREN(age -> age >= 6 && age < 13, new ChildrenFarePolicy()),
    FREE(age -> (age > 0 && age < 6) || age >= 65, new FreeFarePolicy());

    private Predicate<Integer> agePredicate;
    private FarePolicy farePolicy;

    AgeFarePolicy(Predicate<Integer> agePredicate, FarePolicy farePolicy) {
        this.agePredicate = agePredicate;
        this.farePolicy = farePolicy;
    }

    public static FarePolicy findAgeFarePolicy(int age) {
        return  Arrays.stream(AgeFarePolicy.values())
                    .filter(ageFarePolicy -> ageFarePolicy.agePredicate.test(age))
                    .map(AgeFarePolicy::getFarePolicy)
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
    }

    public Predicate<Integer> getAgePredicate() {
        return agePredicate;
    }

    public FarePolicy getFarePolicy() {
        return farePolicy;
    }
}
